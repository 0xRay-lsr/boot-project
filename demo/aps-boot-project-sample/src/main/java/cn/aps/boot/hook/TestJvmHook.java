package cn.aps.boot.hook;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Description :测试jvm hook 执行实际
 * @Author : lishirui
 * @Date ：2024/12/2 16:52
 */
@Component
public class TestJvmHook {

    public TestJvmHook() {
        // 添加hook thread，重写其run方法
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("this is hook demo...=====================");
                // jvm 退出的钩子逻辑
            }
        });
    }

    @Bean
    public TestJvmHookBean testJvmHookBean() {
        // 添加hook thread，重写其run方法
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("bean register demo...=====================");
                // jvm 退出的钩子逻辑
            }
        });
        return new TestJvmHookBean();
    }
}
