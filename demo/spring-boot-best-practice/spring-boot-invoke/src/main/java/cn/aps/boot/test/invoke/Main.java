package cn.aps.boot.test.invoke;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; i++) {
            executorService.execute(new MyRunnable());
        }
    }

    public static void costTest() {
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long cost = System.currentTimeMillis() - startTime;
        System.out.println("开始时间为：" + startTime + " 线程id：" + Thread.currentThread().getName() + " 耗时：" + cost);
    }

    public static class MyRunnable implements Runnable {
        @Override
        public void run() {
            costTest();
        }
    }
}