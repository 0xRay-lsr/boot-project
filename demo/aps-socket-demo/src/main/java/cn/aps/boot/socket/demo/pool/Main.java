package cn.aps.boot.socket.demo.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Description 连接池功能演示的主类
 * @Author lishirui
 * @Date 2025/8/18
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        // 1. 启动一个模拟服务器
        MockServer mockServer = new MockServer();
        mockServer.start();
        int serverPort = mockServer.getPort();
        logger.info("Mock server started on port: {}", serverPort);

        // 等待服务器完全启动
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 2. 获取连接池管理器
        SocketConnectionPoolManager manager = SocketConnectionPoolManager.getInstance();

        // 3. 创建连接池配置
        PoolConfig config = new PoolConfig.Builder("localhost", serverPort)
                .maxPoolSize(5)
                .connectTimeoutMillis(3000)
                .idleTimeoutMillis(10000) // 10秒空闲超时，方便演示
                .monitorIntervalMillis(5000) // 5秒监控周期
                .build();

        // 4. 获取或创建连接池
        String poolName = "my-test-pool";
        SocketConnectionPool pool = manager.getOrCreatePool(poolName, config);

        // 5. 演示借用和归还
        logger.info("--- Demonstrating basic borrow and release ---");
        SocketConnection connection = null;
        try {
            logger.info("Attempting to borrow a connection...");
            connection = pool.borrow(5, TimeUnit.SECONDS);
            logger.info("Connection borrowed successfully: {}", connection.getSocketInfo());
            logger.info("Pool stats: Total={}, Active={}, Idle={}", pool.getTotalCount(), pool.getActiveCount(), pool.getIdleCount());

            // 模拟业务操作：发送一条消息并接收回显
            Socket socket = connection.getSocket();
            OutputStream out = socket.getOutputStream();
            out.write("Hello Pool\n".getBytes());
            out.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = reader.readLine();
            logger.info("Received response from server: {}", response);
        } catch (Exception e) {
            logger.error("An error occurred during socket operation.", e);
        } finally {
            if (connection != null) {
                logger.info("Releasing connection: {}", connection.getSocketInfo());
                pool.release(connection);
                logger.info("Connection released. Pool stats: Total={}, Active={}, Idle={}", pool.getTotalCount(), pool.getActiveCount(), pool.getIdleCount());
            }
        }

        // 6. 最终关闭资源
        logger.info("--- Shutting down ---");
        manager.shutdownAll();
        mockServer.stop();
        logger.info("Shutdown complete.");
    }

    /**
     * 模拟服务器，用于响应心跳和业务请求
     */
    private static class MockServer {
        private ServerSocket serverSocket;
        private ExecutorService clientHandlerPool;
        private volatile boolean running = false;

        public void start() {
            try {
                serverSocket = new ServerSocket(0); // 0表示随机选择一个可用端口
                running = true;
                clientHandlerPool = Executors.newCachedThreadPool();

                Thread acceptorThread = new Thread(() -> {
                    while (running && !serverSocket.isClosed()) {
                        try {
                            Socket clientSocket = serverSocket.accept();
                            clientHandlerPool.submit(() -> handleClient(clientSocket));
                        } catch (IOException e) {
                            if (running) {
                                logger.error("Error accepting client connection", e);
                            }
                        }
                    }
                });
                acceptorThread.setName("MockServer-Acceptor");
                acceptorThread.setDaemon(true);
                acceptorThread.start();
            } catch (IOException e) {
                throw new RuntimeException("Failed to start MockServer", e);
            }
        }

        private void handleClient(Socket clientSocket) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 OutputStream out = clientSocket.getOutputStream()) {

                String line;
                while ((line = reader.readLine()) != null) {
                    logger.debug("MockServer received: {}", line);
                    if ("PING".equalsIgnoreCase(line.trim())) {
                        out.write("PONG\n".getBytes());
                    } else {
                        out.write(("Echo: " + line + "\n").getBytes());
                    }
                    out.flush();
                }
            } catch (IOException e) {
                // 客户端断开连接
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException ignored) {}
            }
        }

        public void stop() {
            running = false;
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                logger.error("Error closing server socket", e);
            }
            if (clientHandlerPool != null) {
                clientHandlerPool.shutdownNow();
            }
        }

        public int getPort() {
            return serverSocket != null ? serverSocket.getLocalPort() : -1;
        }
    }
}
