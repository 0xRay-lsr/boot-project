package cn.aps.boot.logs.core.aggregator;

import cn.aps.boot.logs.api.aggregator.LogEvent;
import cn.aps.boot.logs.api.spi.SPIMeta;

/**
 * @Description : 日志聚合默认空实现
 * @Author : lishirui
 * @Date ：2025/4/14 15:31
 */
@SPIMeta(id = "default")
public class DefaultLogAggregator extends AbstractLogAggregator {
    @Override
    protected void processEvent(LogEvent event) {
        //TODO 空实现
    }
}
