package cn.aps.boot.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/6/4 10:19
 */
public class VirtualThread {
    public static final Semaphore submitLimiter = new Semaphore(3);
    public static void main(String[] args) throws Exception {
        Long startTime = System.currentTimeMillis();
        Thread.ofVirtual().name("lsr-virtual-thread").start(() -> {
            System.out.println(Thread.currentThread().getName()+"is running task 1.");
            try {
                Thread.sleep(2000);
            }catch (Throwable e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName()+"is finished task 1.");
        });

        ThreadFactory threadFactory = Thread.ofVirtual().name("lsr-virtual-thread-",0).factory();
        try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(threadFactory)){
            IntStream.range(0, 10).forEach(i -> {
                executorService.submit(()->{
                    try {
                        submitLimiter.acquire();
                        System.out.println(Thread.currentThread().getName()+" is running task "+i);
                        Thread.sleep(2000);
                    }catch (Throwable e) {
                        Thread.currentThread().interrupt();
                    }finally {
                        submitLimiter.release();
                    }
                });
            });
        }

        Thread.sleep(50000);
    }
}
