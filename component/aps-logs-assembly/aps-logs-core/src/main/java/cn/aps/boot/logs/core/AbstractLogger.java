package cn.aps.boot.logs.core;

import cn.aps.boot.logs.api.Logger;
import cn.aps.boot.logs.api.aggregator.LogAggregator;
import cn.aps.boot.logs.api.aggregator.LogEvent;
import cn.aps.boot.logs.api.config.AggregatorConfig;
import cn.aps.boot.logs.core.aggregator.LogAggregatorFactory;

import java.util.Map;

/**
 * @Description : 抽象logger实现
 * @Author : lishirui
 * @Date ：2025/3/27 19:22
 */
public abstract class AbstractLogger implements Logger {
    // 输出日志格式
    String linkLogPattern = "%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n";

    protected final String name;
    protected LogAggregator aggregator;
    
    protected AbstractLogger(String name) {
        this.name = name;
    }
    
    /**
     * 设置日志聚合器
     * @param type 聚合器类型
     * @param config 配置信息
     */
    public void setAggregator(String type, Map<String, String> config) {
        this.aggregator = LogAggregatorFactory.getAggregator(type, config);
    }

    /**
     * 设置日志聚合器
     * @param config 配置信息
     */
    public void setAggregator(AggregatorConfig config) {
        this.aggregator = LogAggregatorFactory.getAggregator(config.getSpiId(), config);
    }
    
    /**
     * 发送日志到聚合器
     * @param level 日志级别
     * @param message 日志消息
     * @param throwable 异常信息
     */
    protected void sendToAggregator(String level, String message, Throwable throwable) {
        if (aggregator != null) {
            LogEvent event = new LogEvent(name, level, message);
            event.setThrowable(throwable);
            aggregator.send(event);
        }
    }
    
    @Override
    public String getName() {
        return name;
    }

    protected void link() {

    }
}
