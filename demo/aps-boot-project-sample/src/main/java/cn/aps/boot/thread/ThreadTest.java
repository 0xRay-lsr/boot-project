package cn.aps.boot.thread;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/1/13 12:33
 */
public class ThreadTest {
    public static void main(String[] args){
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future<String>> futures = new ArrayList<>();
       try {
           for (int i = 0; i <10 ; i++) {
               Future<String> submit = executorService.submit(new TestCallable());
               futures.add(submit);
           }
           for (int i = 0; i < futures.size(); i++) {
               Future<String> stringFuture = futures.get(i);
               String s = stringFuture.get();
               System.out.println("第"+i+"个处理结果："+s);
           }
       }catch (Exception exception){
           System.out.println(exception);
       }finally {
           executorService.shutdown();
       }
    }
    public static class TestCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            return ""+Thread.currentThread().getName();
        }
    }
}
