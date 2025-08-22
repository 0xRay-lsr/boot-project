package cn.aps.boot.socket.demo.pool;

import cn.aps.boot.socket.demo.pool.exception.NoConnectionAvailableException;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description : Socket连接池测试类 (针对 'pool' 包)
 * @Author : Gemini
 * @Date : 2025/8/19
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SocketPoolTest {
    private static final Logger logger = LoggerFactory.getLogger(SocketPoolTest.class);

    // 测试服务器
    private static MockServer mockServer;
    private SocketConnectionPool pool;

    // 测试配置
    private static final String TEST_HOST = "localhost";
    private static final int MAX_POOL_SIZE = 10; // 增加池大小以适应并发测试
    private static final int CONNECT_TIMEOUT = 3000;
    private static final long IDLE_TIMEOUT = 5000;
    private static final long LEASE_TIMEOUT = 10000;
    private static final int VALIDATION_TIMEOUT = 1000;
    private static final long MONITOR_INTERVAL = 2000;

    @BeforeAll
    static void startServer() throws IOException {
        // 启动模拟服务器
        mockServer = new MockServer();
        mockServer.start();
        logger.info("Mock server started on port: {}", mockServer.getPort());
    }

    @AfterAll
    static void stopServer() {
        if (mockServer != null) {
            mockServer.stop();
        }
        logger.info("Mock server stopped.");
    }

    @BeforeEach
    void setUp() {
        // 创建连接池配置
        PoolConfig config = new PoolConfig.Builder(TEST_HOST, mockServer.getPort())
                .maxPoolSize(MAX_POOL_SIZE)
                .connectTimeoutMillis(CONNECT_TIMEOUT)
                .idleTimeoutMillis(IDLE_TIMEOUT)
                .connectionLeaseMillis(LEASE_TIMEOUT)
                .validationTimeoutMillis(VALIDATION_TIMEOUT)
                .monitorIntervalMillis(MONITOR_INTERVAL)
                .build();

        // 创建连接池
        pool = new SocketConnectionPool(config);
        logger.info("Test setup completed - Pool created for port: {}", mockServer.getPort());
    }

    @AfterEach
    void tearDown() {
        if (pool != null) {
            pool.close();
        }
        logger.info("Test teardown completed");
    }

    @Test
    @Order(1)
    @DisplayName("基本功能测试 - 借出和归还连接")
    void testBasicBorrowAndRelease() throws Exception {
        logger.info("=== 测试基本借出和归还功能 ===");

        // 借出连接
        SocketConnection conn = pool.borrow(5, TimeUnit.SECONDS);
        assertNotNull(conn, "借出的连接不应为空");
        assertTrue(conn.isValid(), "连接应该是有效的");

        // 验证池状态
        assertEquals(1, pool.getActiveCount(), "活跃连接数应为1");
        assertEquals(1, pool.getTotalCount(), "总连接数应为1");

        // 归还连接
        pool.release(conn);

        // 验证归还后状态
        assertEquals(0, pool.getActiveCount(), "归还后活跃连接数应为0");
        assertEquals(1, pool.getIdleCount(), "空闲连接数应为1");

        logger.info("基本功能测试通过");
    }

    @Test
    @Order(2)
    @DisplayName("并发测试 - 验证并发修复和性能优化")
    void testConcurrentBorrowAndRelease() throws Exception {
        logger.info("=== 测试并发借出和归还 ===");

        int threadCount = 20; // 增加线程数以产生更大压力
        int operationsPerThread = 50;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // 启动并发测试线程
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await(); // 等待统一开始信号

                    for (int j = 0; j < operationsPerThread; j++) {
                        SocketConnection conn = null;
                        try {
                            // 借出连接
                            conn = pool.borrow(5, TimeUnit.SECONDS);
                            assertNotNull(conn, "连接不应为空");

                            // 模拟使用连接
                            Thread.sleep(5 + (int)(Math.random() * 20));

                            successCount.incrementAndGet();

                        } catch (Exception e) {
                            logger.error("线程 {} 操作 {} 失败: {}", threadId, j, e.getMessage());
                            errorCount.incrementAndGet();
                        } finally {
                            if (conn != null) {
                                pool.release(conn);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        // 开始并发测试
        startLatch.countDown();

        // 等待所有线程完成
        assertTrue(doneLatch.await(30, TimeUnit.SECONDS), "并发测试应在30秒内完成");

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // 验证结果
        int expectedOperations = threadCount * operationsPerThread;
        logger.info("并发测试结果: 成功={}, 失败={}, 期望={}",
                successCount.get(), errorCount.get(), expectedOperations);
        logger.info("最终池状态: Total={}, Active={}, Idle={}",
                pool.getTotalCount(), pool.getActiveCount(), pool.getIdleCount());

        // 断言：不应该有任何失败
        assertEquals(0, errorCount.get(), "并发测试中不应有任何错误");
        assertEquals(expectedOperations, successCount.get(), "所有操作都应该成功");

        // 最终池状态应该平衡
        assertEquals(0, pool.getActiveCount(), "测试结束后活跃连接数应为0");
        // 总连接数应该小于等于池的最大大小
        assertTrue(pool.getTotalCount() <= MAX_POOL_SIZE, "总连接数不应超过池大小限制");
        assertEquals(pool.getTotalCount(), pool.getIdleCount(), "空闲连接数应等于总连接数");
    }

    @Test
    @Order(3)
    @DisplayName("池大小限制测试")
    void testPoolSizeLimit() throws Exception {
        logger.info("=== 测试池大小限制 ===");

        List<SocketConnection> connections = new ArrayList<>();

        // 借出所有可用连接
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            SocketConnection conn = pool.borrow(1, TimeUnit.SECONDS);
            assertNotNull(conn, "第" + (i + 1) + "个连接应该成功借出");
            connections.add(conn);
        }

        assertEquals(MAX_POOL_SIZE, pool.getActiveCount(), "活跃连接数应等于池大小限制");

        // 尝试借出超出限制的连接，应该超时
        assertThrows(NoConnectionAvailableException.class, () -> {
            pool.borrow(500, TimeUnit.MILLISECONDS);
        }, "超出池大小限制时应抛出异常");

        // 清理
        for(SocketConnection conn : connections) {
            pool.release(conn);
        }

        logger.info("池大小限制测试通过");
    }


    /**
     * 模拟服务器，用于测试
     */
    private static class MockServer {
        private ServerSocket serverSocket;
        private ExecutorService service;
        private volatile boolean running = false;

        public void start() throws IOException {
            serverSocket = new ServerSocket(0); // 使用随机端口
            running = true;
            service = Executors.newCachedThreadPool();
            service.submit(() -> {
                while (running && !serverSocket.isClosed()) {
                    try {
                        Socket client = serverSocket.accept();
                        service.submit(() -> handleClient(client));
                    } catch (IOException e) {
                        if (running) {
                            logger.debug("Mock server accept error: {}", e.getMessage());
                        }
                    }
                }
            });
        }

        private void handleClient(Socket client) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 OutputStream out = client.getOutputStream()) {
                String line;
                while (running && !client.isClosed() && (line = reader.readLine()) != null) {
                    if ("PING".equalsIgnoreCase(line.trim())) {
                        out.write("PONG\n".getBytes());
                        out.flush();
                    }
                }
            } catch (IOException e) {
                // 客户端断开
            } finally {
                try {
                    client.close();
                } catch (IOException ignored) {}
            }
        }

        public int getPort() {
            return serverSocket != null ? serverSocket.getLocalPort() : -1;
        }

        public void stop() {
            running = false;
            if (service != null) {
                service.shutdownNow();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    logger.debug("Error closing mock server: {}", e.getMessage());
                }
            }
        }
    }
}
