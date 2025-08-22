package cn.aps.boot.socket.demo.pool;

import cn.aps.boot.socket.demo.pool.exception.NoConnectionAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description 单个目标的Socket连接池
 *              <p>
 *              此类为一个特定的目标 (host:port) 管理一个Socket连接池。
 *              它负责连接的创建、验证、借出、归还以及生命周期管理（空闲清理、泄漏检测）。
 *              整个类是线程安全的。
 *              </p>
 * @Author lishirui
 * @Date 2025/8/18
 */
public class SocketConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(SocketConnectionPool.class);

    // --- 配置属性 ---
    private final String host;
    private final int port;
    private final int maxPoolSize;
    private final int connectTimeoutMillis;
    private final long idleTimeoutMillis;
    private final long connectionLeaseMillis;
    private final int validationTimeoutMillis;

    // --- 核心数据结构 ---
    private final BlockingQueue<SocketConnection> idleConnections;
    private final ConcurrentHashMap<SocketConnection, Long> borrowedConnections;

    // --- 状态与锁 ---
    private final AtomicInteger totalConnections = new AtomicInteger(0);
    private volatile boolean isClosed = false;

    // --- 后台线程池 ---
    private final ScheduledExecutorService monitorExecutor;
    private final ExecutorService connectionCreator;
    private final ScheduledExecutorService leaseScheduler; // 用于处理租约超时的共享调度器

    /**
     * 构造一个Socket连接池实例。
     *
     * @param config 连接池的配置对象
     */
    public SocketConnectionPool(PoolConfig config) {
        this.host = config.getHost();
        this.port = config.getPort();
        this.maxPoolSize = config.getMaxPoolSize();
        this.connectTimeoutMillis = config.getConnectTimeoutMillis();
        this.idleTimeoutMillis = config.getIdleTimeoutMillis();
        this.connectionLeaseMillis = config.getConnectionLeaseMillis();
        this.validationTimeoutMillis = config.getValidationTimeoutMillis();

        this.idleConnections = new LinkedBlockingQueue<>(maxPoolSize);
        this.borrowedConnections = new ConcurrentHashMap<>(maxPoolSize);

        // 初始化后台线程池
        this.leaseScheduler = Executors.newScheduledThreadPool(
                Math.max(2, Runtime.getRuntime().availableProcessors() / 2),
                r -> new Thread(r, "Pool-Lease-Scheduler-" + host + ":" + port)
        );
        this.monitorExecutor = Executors.newSingleThreadScheduledExecutor(
                r -> new Thread(r, "Pool-Monitor-" + host + ":" + port)
        );
        this.connectionCreator = Executors.newFixedThreadPool(
                2, r -> new Thread(r, "Pool-Creator-" + host + ":" + port)
        );

        // 启动后台监控任务
        this.monitorExecutor.scheduleAtFixedRate(
                this::maintainPool,
                config.getMonitorIntervalMillis(),
                config.getMonitorIntervalMillis(),
                TimeUnit.MILLISECONDS
        );

        logger.info("SocketConnectionPool for {}:{} initialized with max size {}.", host, port, maxPoolSize);
    }

    /**
     * 从连接池借出一个连接。
     *
     * @param timeout 等待的超时时间
     * @param unit    时间单位
     * @return 一个有效的 SocketConnection
     * @throws InterruptedException         如果线程在等待时被中断
     * @throws NoConnectionAvailableException 如果在超时时间内无法获取连接
     */
    public SocketConnection borrow(long timeout, TimeUnit unit) throws InterruptedException, NoConnectionAvailableException {
        checkNotClosed();
        long deadline = System.nanoTime() + unit.toNanos(timeout);

        while (System.nanoTime() < deadline) {
            // 1. 尝试从空闲队列获取并校验
            SocketConnection conn = idleConnections.poll();
            if (conn != null) {
                if (conn.isValid()) {
                    return registerBorrowed(conn);
                } else {
                    // 如果连接无效，则关闭并继续循环尝试
                    closeAndRemove(conn, "invalid connection polled from idle queue");
                    continue;
                }
            }

            // 2. 尝试创建新连接（如果池未满）
            if (totalConnections.get() < maxPoolSize) {
                conn = createNewConnection();
                if (conn != null) {
                    return registerBorrowed(conn);
                }
            }

            // 3. 阻塞等待空闲连接
            long remainingNanos = deadline - System.nanoTime();
            if (remainingNanos <= 0) {
                break; // 超时，跳出循环
            }
            // 阻塞等待，循环开始时会再次校验
            conn = idleConnections.poll(remainingNanos, TimeUnit.NANOSECONDS);
            if (conn != null) {
                if (conn.isValid()) {
                    return registerBorrowed(conn);
                } else {
                    closeAndRemove(conn, "invalid connection polled from idle queue");
                    // 继续循环
                }
            }
        }

        throw new NoConnectionAvailableException("Timeout waiting for an available connection.");
    }

    /**
     * 归还一个连接到池中。
     *
     * @param conn 要归还的连接
     */
    public void release(SocketConnection conn) {
        if (conn == null) {
            return;
        }

        // 从借出列表移除
        if (borrowedConnections.remove(conn) == null) {
            // 如果一个连接不在借出列表，说明它可能已被租约超时强制关闭，或被重复归还
            logger.warn("Releasing a connection that was not in the borrowed list: {}", conn.getSocketInfo());
            return;
        }

        if (isClosed) {
            closeAndRemove(conn, "pool is closed");
            return;
        }

        // 归还前检查有效性
        if (!conn.isValid()) {
            closeAndRemove(conn, "invalid on release");
            return;
        }

        // 尝试放入空闲队列
        if (!idleConnections.offer(conn)) {
            // 如果队列已满，则关闭此连接
            closeAndRemove(conn, "idle queue full on release");
        } else {
            conn.updateLastUsedTime();
        }
    }

    /**
     * 关闭连接池并释放所有资源。
     */
    public void close() {
        if (isClosed) {
            return;
        }
        isClosed = true;
        logger.info("Shutting down SocketConnectionPool for {}:{}...", host, port);

        // 关闭后台线程池
        monitorExecutor.shutdownNow();
        connectionCreator.shutdownNow();
        leaseScheduler.shutdownNow();

        // 关闭所有空闲和借出的连接
        List<SocketConnection> allConnections = new ArrayList<>();
        idleConnections.drainTo(allConnections);
        allConnections.addAll(borrowedConnections.keySet());

        for (SocketConnection conn : allConnections) {
            conn.forceClose();
        }

        borrowedConnections.clear();
        totalConnections.set(0);
        logger.info("SocketConnectionPool for {}:{} closed.", host, port);
    }


    // --- 内部辅助方法 ---

    private SocketConnection pollIdleAndValidate() {
        SocketConnection conn;
        while ((conn = idleConnections.poll()) != null) {
            if (conn.isValid()) {
                return conn;
            }
            closeAndRemove(conn, "invalid connection from idle queue");
        }
        return null;
    }

    private SocketConnection createNewConnection() {
        // 使用 CAS 无锁地预定一个创建名额
        while (true) {
            int currentCount = totalConnections.get();
            if (currentCount >= maxPoolSize) {
                return null; // 池已满
            }
            // 尝试原子性地将连接数+1
            if (totalConnections.compareAndSet(currentCount, currentCount + 1)) {
                break; // 成功预定名额，跳出循环去创建连接
            }
            // 如果 CAS 失败，说明有其他线程修改了计数值，循环重试
        }

        // 在锁外执行耗时的网络IO操作
        try {
            Socket socket = new Socket();
            socket.setSoTimeout(validationTimeoutMillis);
            socket.setTcpNoDelay(true);
            socket.setKeepAlive(true);
            socket.connect(new InetSocketAddress(host, port), connectTimeoutMillis);

            SocketConnection conn = new SocketConnection(socket, connectionLeaseMillis, leaseScheduler);
            if (conn.isValid()) {
                logger.debug("Created new connection: {}", conn.getSocketInfo());
                return conn;
            } else {
                conn.forceClose();
                throw new IOException("Newly created connection failed validation.");
            }
        } catch (IOException e) {
            // 如果创建失败，必须将预定的名额还回去
            totalConnections.decrementAndGet();
            logger.error("Failed to create new socket connection to {}:{}: {}", host, port, e.getMessage());
            return null;
        }
    }

    private SocketConnection registerBorrowed(SocketConnection conn) {
        conn.updateLastUsedTime();
        borrowedConnections.put(conn, System.currentTimeMillis());
        return conn;
    }

    private void closeAndRemove(SocketConnection conn, String reason) {
        conn.forceClose();
        totalConnections.decrementAndGet();
        borrowedConnections.remove(conn); // 确保也从借出列表移除
        logger.debug("Connection {} closed and removed. Reason: {}.", conn.getSocketInfo(), reason);
    }

    private void maintainPool() {
        if (isClosed) {
            return;
        }
        logger.trace("Running pool maintenance for {}:{}...", host, port);
        cleanupIdleConnections();
        checkLeakedConnections();
    }

    private void cleanupIdleConnections() {
        // 使用 drainTo 将元素原子性地移出，避免并发修改问题
        List<SocketConnection> idleList = new ArrayList<>();
        idleConnections.drainTo(idleList);

        long now = System.currentTimeMillis();
        for (SocketConnection conn : idleList) {
            // 检查空闲超时
            if (now - conn.getLastUsedTime() > idleTimeoutMillis) {
                closeAndRemove(conn, "idle timeout");
                continue;
            }
            // 检查连接是否依然有效
            if (!conn.isValid()) {
                closeAndRemove(conn, "invalid idle connection");
                continue;
            }
            // 如果连接有效且未超时，则放回池中
            if (!idleConnections.offer(conn)) {
                // 理论上不应该发生，因为我们刚清空了队列
                closeAndRemove(conn, "failed to requeue idle connection");
            }
        }
    }

    private void checkLeakedConnections() {
        long now = System.currentTimeMillis();
        // 租约时长的1.5倍作为泄漏判断阈值
        long leakThreshold = (long) (connectionLeaseMillis * 1.5);

        borrowedConnections.forEach((conn, borrowTime) -> {
            if (now - borrowTime > leakThreshold) {
                logger.warn("Detected leaked connection (borrowed for {}ms): {}", now - borrowTime, conn.getSocketInfo());
                // 泄漏的连接也需要被移除和关闭
                closeAndRemove(conn, "connection leak");
            }
        });
    }

    private void checkNotClosed() {
        if (isClosed) {
            throw new IllegalStateException("SocketConnectionPool is closed.");
        }
    }

    // --- Getters for monitoring ---
    public int getTotalCount() {
        return totalConnections.get();
    }

    public int getIdleCount() {
        return idleConnections.size();
    }

    public int getActiveCount() {
        return borrowedConnections.size();
    }
}
