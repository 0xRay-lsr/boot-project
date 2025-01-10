package cn.aps.boot.listener;

import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description : 表示 Spring 应用上下文已被初始化。这发生在 Spring 上下文的生命周期的非常早期阶段，应用上下文已经创建但尚未启动。
 * @Author : lishirui
 * @Date ：2025/1/6 14:30
 */
@Component
public class ApplicationContextInitializedEventListenerTest implements ApplicationListener<ApplicationContextInitializedEvent> {

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        System.out.println("Spring Context Initialized!");
    }
}
