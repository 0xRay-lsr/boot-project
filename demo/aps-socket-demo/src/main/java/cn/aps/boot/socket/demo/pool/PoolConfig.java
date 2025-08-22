package cn.aps.boot.socket.demo.pool;

/**
 * @Description Socket连接池的配置类
 *              <p>
 *              这是一个数据类，用于封装创建 {@link SocketConnectionPool} 所需的所有配置参数。
 *              使用建造者模式 (Builder Pattern) 可以方便地创建不可变的配置对象。
 *              </p>
 * @Author lishirui
 * @Date 2025/8/18
 */
public class PoolConfig {

    private final String host;
    private final int port;
    private final int maxPoolSize;
    private final int connectTimeoutMillis;
    private final long idleTimeoutMillis;
    private final long connectionLeaseMillis;
    private final int validationTimeoutMillis;
    private final long monitorIntervalMillis;

    // 私有构造函数，强制通过Builder创建
    private PoolConfig(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.maxPoolSize = builder.maxPoolSize;
        this.connectTimeoutMillis = builder.connectTimeoutMillis;
        this.idleTimeoutMillis = builder.idleTimeoutMillis;
        this.connectionLeaseMillis = builder.connectionLeaseMillis;
        this.validationTimeoutMillis = builder.validationTimeoutMillis;
        this.monitorIntervalMillis = builder.monitorIntervalMillis;
    }

    // --- Getters ---

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public long getIdleTimeoutMillis() {
        return idleTimeoutMillis;
    }

    public long getConnectionLeaseMillis() {
        return connectionLeaseMillis;
    }

    public int getValidationTimeoutMillis() {
        return validationTimeoutMillis;
    }

    public long getMonitorIntervalMillis() {
        return monitorIntervalMillis;
    }

    // --- Builder Class ---

    public static class Builder {
        private String host = "localhost";
        private int port = 8080;
        private int maxPoolSize = 10;
        private int connectTimeoutMillis = 5000; // 5 seconds
        private long idleTimeoutMillis = 60000; // 1 minute
        private long connectionLeaseMillis = 300000; // 5 minutes
        private int validationTimeoutMillis = 2000; // 2 seconds
        private long monitorIntervalMillis = 30000; // 30 seconds

        public Builder(String host, int port) {
            if (host == null || host.trim().isEmpty()) {
                throw new IllegalArgumentException("Host cannot be null or empty.");
            }
            if (port <= 0 || port > 65535) {
                throw new IllegalArgumentException("Port must be between 1 and 65535.");
            }
            this.host = host;
            this.port = port;
        }

        public Builder maxPoolSize(int maxPoolSize) {
            if (maxPoolSize <= 0) throw new IllegalArgumentException("maxPoolSize must be > 0");
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public Builder connectTimeoutMillis(int connectTimeoutMillis) {
            if (connectTimeoutMillis <= 0) throw new IllegalArgumentException("connectTimeoutMillis must be > 0");
            this.connectTimeoutMillis = connectTimeoutMillis;
            return this;
        }

        public Builder idleTimeoutMillis(long idleTimeoutMillis) {
            if (idleTimeoutMillis <= 0) throw new IllegalArgumentException("idleTimeoutMillis must be > 0");
            this.idleTimeoutMillis = idleTimeoutMillis;
            return this;
        }

        public Builder connectionLeaseMillis(long connectionLeaseMillis) {
            if (connectionLeaseMillis <= 0) throw new IllegalArgumentException("connectionLeaseMillis must be > 0");
            this.connectionLeaseMillis = connectionLeaseMillis;
            return this;
        }

        public Builder validationTimeoutMillis(int validationTimeoutMillis) {
            if (validationTimeoutMillis <= 0) throw new IllegalArgumentException("validationTimeoutMillis must be > 0");
            this.validationTimeoutMillis = validationTimeoutMillis;
            return this;
        }

        public Builder monitorIntervalMillis(long monitorIntervalMillis) {
            if (monitorIntervalMillis <= 0) throw new IllegalArgumentException("monitorIntervalMillis must be > 0");
            this.monitorIntervalMillis = monitorIntervalMillis;
            return this;
        }

        public PoolConfig build() {
            return new PoolConfig(this);
        }
    }
}
