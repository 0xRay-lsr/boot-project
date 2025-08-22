package cn.aps.boot.test.watchdog;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 超时看门狗服务
 * 使用一个独立的后台线程来监控被@Timeout注解标记的业务线程。
 *
 * @Author lishirui
 * @Date 2025-08-21
 */
@Component
public class TimeoutWatchdog implements Runnable {

    private final ConcurrentHashMap<Thread, Long> monitoredThreads = new ConcurrentHashMap<>();
    private volatile boolean running = true;
    private Thread watchdogThread;

    // 看门狗的检查间隔，单位毫秒
    private static final long CHECK_INTERVAL = 100L;

    @PostConstruct
    private void start() {
        watchdogThread = new Thread(this, "TimeoutWatchdogThread");
        watchdogThread.setDaemon(true);
        watchdogThread.start();
    }

    @PreDestroy
    private void stop() {
        this.running = false;
        if (watchdogThread != null) {
            watchdogThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                monitoredThreads.forEach((thread, deadline) -> {
                    if (System.currentTimeMillis() > deadline) {
                        if (thread.isAlive()) {
                            thread.interrupt(); // 发送中断信号
                        }
                        monitoredThreads.remove(thread);
                    }
                });
                TimeUnit.MILLISECONDS.sleep(CHECK_INTERVAL);
            } catch (InterruptedException e) {
                // 当应用关闭，stop()方法被调用时，会进入这里
                Thread.currentThread().interrupt();
                this.running = false;
            }
        }
    }

    /**
     * 注册一个需要被监控的线程
     * @param thread 业务线程
     * @param timeoutMillis 超时毫秒数
     */
    public void monitor(Thread thread, long timeoutMillis) {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        monitoredThreads.put(thread, deadline);
    }

    /**
     * 停止对一个线程的监控（业务方法执行完毕时调用）
     * @param thread 业务线程
     */
    public void unmonitor(Thread thread) {
        monitoredThreads.remove(thread);
    }
}
