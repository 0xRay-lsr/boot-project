package cn.aps.boot.socket.demo.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description Socket连接的包装器
 *              <p>
 *              此类封装了 {@link java.net.Socket}，为其增加了生命周期管理、状态跟踪和心跳验证机制。
 *              主要设计用于在连接池 ({@link SocketConnectionPool}) 中进行管理。
 *              </p>
 * @Author lishirui
 * @Date 2025/8/18
 */
public class SocketConnection {

    private static final Logger logger = LoggerFactory.getLogger(SocketConnection.class);

    private final Socket socket;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private volatile long lastUsedTime;
    private final long creationTime;

    // 用于执行租约超时任务的调度器任务句柄
    private final ScheduledFuture<?> leaseTimeoutTask;

    /**
     * 构造一个SocketConnection实例。
     *
     * @param socket      底层的Java Socket对象，不能为null。
     * @param leaseMillis 连接的租约时长（毫秒）。如果大于0，则在指定时间后连接将被强制关闭。
     * @param scheduler   一个共享的 {@link ScheduledExecutorService} 实例，用于调度租约超时任务。
     *                    传入共享的调度器可以避免为每个连接都创建一个独立的Timer线程，从而提高效率。
     */
    public SocketConnection(Socket socket, long leaseMillis, ScheduledExecutorService scheduler) {
        if (socket == null) {
            throw new IllegalArgumentException("Socket cannot be null");
        }
        this.socket = socket;
        this.lastUsedTime = System.currentTimeMillis();
        this.creationTime = this.lastUsedTime;

        // 如果设置了租约时长并且调度器不为空，则安排一个租约超时任务
        if (leaseMillis > 0 && scheduler != null) {
            this.leaseTimeoutTask = scheduler.schedule(() -> {
                // 任务执行时，如果连接尚未关闭，则强制关闭它
                if (this.closed.compareAndSet(false, true)) {
                    logger.warn("Connection lease expired, force closing: {}", getSocketInfo());
                    doClose("Lease timeout");
                }
            }, leaseMillis, TimeUnit.MILLISECONDS);
        } else {
            this.leaseTimeoutTask = null;
        }
    }

    /**
     * 检查连接是否仍然有效。
     * <p>
     * 这是一个应用层心跳检测。它会：
     * 1. 检查基础的连接状态（是否关闭、是否连接）。
     * 2. 设置一个短暂的读取超时。
     * 3. 发送一个 "PING" 消息。
     * 4. 期望在超时时间内收到一个 "PONG" 响应。
     * </p>
     *
     * @return 如果连接有效且心跳检测成功，返回 true；否则返回 false。
     */
    public synchronized boolean isValid() {
        // 快速检查，避免不必要的IO操作
        if (closed.get() || socket == null || socket.isClosed() || !socket.isConnected()) {
            return false;
        }

        try {
            // 保存原始的超时设置，并在检查后恢复
            int originalTimeout = socket.getSoTimeout();
            try {
                // 设置一个短暂的超时（例如500毫秒），防止心跳检测长时间阻塞
                socket.setSoTimeout(500);
                OutputStream out = socket.getOutputStream();
                // 发送心跳请求，注意需要换行符作为消息边界
                out.write("PING\n".getBytes());
                out.flush();

                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[16];
                int bytesRead = in.read(buffer);

                // 检查是否收到了有效的响应
                return bytesRead > 0 && new String(buffer, 0, bytesRead).trim().equals("PONG");
            } finally {
                // 无论成功与否，都恢复原始的超时设置
                socket.setSoTimeout(originalTimeout);
            }
        } catch (IOException e) {
            // 任何IO异常都意味着连接可能已损坏
            logger.debug("Connection validation failed for {}: {}", getSocketInfo(), e.getMessage());
            return false;
        }
    }

    /**
     * 正常关闭连接。
     * 这是一个幂等操作，多次调用也只会执行一次关闭逻辑。
     */
    public void close() {
        if (closed.compareAndSet(false, true)) {
            cancelLeaseTask();
            doClose("Regular close");
        }
    }

    /**
     * 强制关闭连接，通常在连接泄漏或异常时由连接池调用。
     * 同样是幂等操作。
     */
    public void forceClose() {
        if (closed.compareAndSet(false, true)) {
            cancelLeaseTask();
            doClose("Force close");
        }
    }

    /**
     * 取消租约超时任务。
     */
    private void cancelLeaseTask() {
        if (leaseTimeoutTask != null) {
            // false表示不中断正在执行的任务（对于我们的场景，任务本身很快，所以无所谓）
            leaseTimeoutTask.cancel(false);
        }
    }

    /**
     * 执行实际的关闭操作。
     *
     * @param reason 关闭原因，用于日志记录。
     */
    private void doClose(String reason) {
        if (socket != null && !socket.isClosed()) {
            try {
                // 优雅地关闭输入和输出流
                socket.shutdownInput();
            } catch (IOException ignored) {
                // 忽略异常
            }
            try {
                socket.shutdownOutput();
            } catch (IOException ignored) {
                // 忽略异常
            }
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("Error closing socket for {}: {}", getSocketInfo(), e.getMessage());
            }
            logger.debug("Socket closed ({}) for: {}", reason, getSocketInfo());
        }
    }

    // --- Getters and Setters ---

    public Socket getSocket() {
        return socket;
    }

    public long getLastUsedTime() {
        return lastUsedTime;
    }

    public void updateLastUsedTime() {
        this.lastUsedTime = System.currentTimeMillis();
    }

    public String getSocketInfo() {
        if (socket == null || socket.getInetAddress() == null) {
            return "uninitialized socket";
        }
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }
}
