package cn.aps.boot.logs.core.aggregator;

import cn.aps.boot.logs.api.aggregator.LogAggregator;
import cn.aps.boot.logs.api.config.AggregatorConfig;
import cn.aps.boot.logs.core.factories.JDKFactoriesLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志聚合器工厂
 */
public class LogAggregatorFactory {
    
    private static final Map<String, LogAggregator> aggregators = new ConcurrentHashMap<>();
    
    /**
     * 获取日志聚合器实例
     * @param type 聚合器类型
     * @param config 配置信息
     * @return 日志聚合器实例
     */
    public static LogAggregator getAggregator(String type, Map<String, String> config) {
        return aggregators.computeIfAbsent(type, k -> {
            LogAggregator aggregator = createAggregator(type);
            if (aggregator != null) {
                aggregator.init(config);
            }
            return aggregator;
        });
    }

    /**
     * 获取日志聚合器实例
     * @param config 配置信息
     * @return 日志聚合器实例
     */
    public static LogAggregator getAggregator(String spiId, AggregatorConfig config) {
        return aggregators.computeIfAbsent(spiId, k -> {
            LogAggregator aggregator = createAggregator(spiId);
            if (aggregator != null) {
                aggregator.init(config);
            }
            return aggregator;
        });
    }
    
    /**
     * 创建日志聚合器
     * @param type 聚合器类型
     * @return 日志聚合器实例
     */
    private static LogAggregator createAggregator(String type) {
        // 扩展jdk spi
        return JDKFactoriesLoader.getFactoryById(LogAggregator.class, type);
    }
    
    /**
     * 关闭所有聚合器
     */
    public static void closeAll() {
        aggregators.values().forEach(LogAggregator::close);
        aggregators.clear();
    }
} 