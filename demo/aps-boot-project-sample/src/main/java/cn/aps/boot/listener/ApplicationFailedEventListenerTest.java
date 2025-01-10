package cn.aps.boot.listener;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description : 表示 Spring Boot 应用启动失败时触发的事件。它会在应用启动失败时被触发，可以用于记录失败日志或执行补救措施。
 * @Author : lishirui
 * @Date ：2025/1/6 14:33
 */
@Component
public class ApplicationFailedEventListenerTest implements ApplicationListener<ApplicationFailedEvent> {

    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        System.out.println("Application failed to start.");
        event.getException().printStackTrace();
    }
}
