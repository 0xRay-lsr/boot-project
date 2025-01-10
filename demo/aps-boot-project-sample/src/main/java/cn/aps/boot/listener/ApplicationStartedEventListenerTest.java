package cn.aps.boot.listener;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description : 表示 Spring Boot 应用启动开始时的事件（在 Spring 应用上下文创建之后，主要用于初始化前的一些操作）。
 * @Author : lishirui
 * @Date ：2025/1/6 14:28
 */
@Component
public class ApplicationStartedEventListenerTest implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("Spring Boot Application is started !");
    }
}
