package cn.aps.boot.socket.demo.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description Socket连接池管理器
 *              <p>
 *              这是一个全局管理器，采用单例模式，用于创建、缓存和管理多个 {@link SocketConnectionPool} 实例。
 *              每个连接池由一个唯一的名称 (通常是 "host:port") 来标识。
 *              </p>
 * @Author lishirui
 * @Date 2025/8/18
 */
public class SocketConnectionPoolManager {

    private static final Logger logger = LoggerFactory.getLogger(SocketConnectionPoolManager.class);

    private final ConcurrentHashMap<String, SocketConnectionPool> pools;

    // --- 单例模式实现 ---
    private static final SocketConnectionPoolManager INSTANCE = new SocketConnectionPoolManager();

    private SocketConnectionPoolManager() {
        this.pools = new ConcurrentHashMap<>();
    }

    /**
     * 获取 SocketConnectionPoolManager 的全局唯一实例。
     *
     * @return 管理器实例
     */
    public static SocketConnectionPoolManager getInstance() {
        return INSTANCE;
    }

    /**
     * 根据指定的配置获取或创建一个新的Socket连接池。
     * <p>
     * 这是一个原子操作。如果具有给定名称的连接池已存在，则直接返回它。
     * 否则，根据提供的配置创建一个新的连接池，将其缓存，然后返回。
     * </p>
     *
     * @param poolName 连接池的唯一名称，推荐使用 "host:port" 格式。
     * @param config   连接池的配置对象。
     * @return 对应名称的 SocketConnectionPool 实例。
     */
    public SocketConnectionPool getOrCreatePool(String poolName, PoolConfig config) {
        if (poolName == null || poolName.trim().isEmpty()) {
            throw new IllegalArgumentException("Pool name cannot be null or empty.");
        }
        // computeIfAbsent 保证了在多线程环境下创建操作的原子性
        return pools.computeIfAbsent(poolName, key -> {
            logger.info("Creating new SocketConnectionPool with name '{}' for target {}:{}",
                    key, config.getHost(), config.getPort());
            return new SocketConnectionPool(config);
        });
    }

    /**
     * 根据名称获取一个已存在的连接池。
     *
     * @param poolName 连接池的名称
     * @return 如果存在，返回 SocketConnectionPool 实例；否则返回 null。
     */
    public SocketConnectionPool getPool(String poolName) {
        return pools.get(poolName);
    }

    /**
     * 关闭并移除指定名称的连接池。
     *
     * @param poolName 要关闭的连接池的名称
     */
    public void closePool(String poolName) {
        SocketConnectionPool pool = pools.remove(poolName);
        if (pool != null) {
            logger.info("Closing and removing pool: {}", poolName);
            pool.close();
        } else {
            logger.warn("Attempted to close a non-existent pool: {}", poolName);
        }
    }

    /**
     * 关闭并移除所有由该管理器创建的连接池。
     * <p>
     * 这个方法应该在应用程序关闭时调用，以确保所有网络资源和后台线程都被正确释放。
     * </p>
     */
    public void shutdownAll() {
        logger.info("Shutting down all connection pools...");
        pools.forEach((name, pool) -> {
            logger.info("Closing pool: {}", name);
            pool.close();
        });
        pools.clear();
        logger.info("All connection pools have been shut down.");
    }

    /**
     * 获取当前管理器中的连接池数量。
     *
     * @return 连接池的数量
     */
    public int getPoolCount() {
        return pools.size();
    }
}
