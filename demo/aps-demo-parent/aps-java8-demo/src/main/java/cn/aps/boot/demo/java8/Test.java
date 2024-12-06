package cn.aps.boot.demo.java8;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2024/9/26 21:10
 */
public class Test {
    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(11), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread();
            }
        }, new ThreadPoolExecutor.AbortPolicy()) {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                System.out.println("beforeExecute");
            }

            @Override
            public void execute(Runnable r) {
                System.out.println("execute");
                super.execute(r);
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                System.out.println("afterExecute");
            }
        };
        Runnable runnable = () -> {
            System.out.println("test");
        };

        threadPoolExecutor.execute(runnable);
        System.out.println("111111");

    }
}
