package cn.aps.boot.logs.api.aggregator;

import cn.aps.boot.logs.api.config.AggregatorConfig;
import cn.aps.boot.logs.api.spi.SPI;

import java.util.Map;

/**
 * 日志聚合器接口
 */
@SPI
public interface LogAggregator {
    
    /**
     * 初始化聚合器
     * @param config 配置信息
     */
    void init(Map<String, String> config);

    /**
     * 初始化聚合器
     * @param config 配置信息
     */
    void init(AggregatorConfig config);

    /**
     * 发送日志
     * @param logEvent 日志事件
     */
    void send(LogEvent logEvent);
    
    /**
     * 关闭聚合器
     */
    void close();
} 