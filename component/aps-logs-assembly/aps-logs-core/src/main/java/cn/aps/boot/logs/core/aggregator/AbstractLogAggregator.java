package cn.aps.boot.logs.core.aggregator;

import cn.aps.boot.logs.api.aggregator.LogAggregator;
import cn.aps.boot.logs.api.aggregator.LogEvent;
import cn.aps.boot.logs.api.config.AggregatorConfig;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 日志聚合器抽象基类
 */
public abstract class AbstractLogAggregator implements LogAggregator {
    
    protected ThreadPoolExecutor executor;
    protected BlockingQueue<LogEvent> eventQueue;
    protected volatile boolean running = true;
    protected int queueSize = 10000;
    protected int corePoolSize = 2;
    protected int maxPoolSize = 4;
    protected long keepAliveTime = 60L;
    
    @Override
    public void init(Map<String, String> config) {
        // 从配置中读取参数
        if (config.containsKey("queueSize")) {
            queueSize = Integer.parseInt(config.get("queueSize"));
        }
        if (config.containsKey("corePoolSize")) {
            corePoolSize = Integer.parseInt(config.get("corePoolSize"));
        }
        if (config.containsKey("maxPoolSize")) {
            maxPoolSize = Integer.parseInt(config.get("maxPoolSize"));
        }
        if (config.containsKey("keepAliveTime")) {
            keepAliveTime = Long.parseLong(config.get("keepAliveTime"));
        }
        start();
    }

    @Override
    public void init(AggregatorConfig config) {
        queueSize = config.getQueueSize();
        corePoolSize = config.getCorePoolSize();
        maxPoolSize = config.getMaxPoolSize();
        keepAliveTime = config.getKeepAliveTime();
        start();
    }

    private void start(){
        // 初始化线程池和队列
        eventQueue = new LinkedBlockingQueue<>(queueSize);
        executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> {
                    Thread t = new Thread(r);
                    t.setName("aps-log-aggregator-" + t.getId());
                    return t;
                }
        );

        // 启动处理线程
        startProcessor();
    }

    @Override
    public void send(LogEvent event) {
        if (running) {
            try {
                eventQueue.offer(event, 100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @Override
    public void close() {
        running = false;
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    protected abstract void processEvent(LogEvent event);
    
    private void startProcessor() {
        executor.submit(() -> {
            while (running || !eventQueue.isEmpty()) {
                try {
                    LogEvent event = eventQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (event != null) {
                        processEvent(event);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    // 处理异常，但不中断处理线程
                    e.printStackTrace();
                }
            }
        });
    }
} 