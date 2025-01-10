package cn.aps.boot.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/1/6 15:56
 */
@Component
public class ApplicationContextStoppedEventTest implements ApplicationListener<ContextStoppedEvent> {
    @Override
    public void onApplicationEvent(ContextStoppedEvent event) {
        System.out.println("Application Context Stopped Event Test!!!");
    }
}
