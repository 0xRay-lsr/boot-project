package cn.aps.boot.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/1/6 15:55
 */
@Component
public class ApplicationContextClosedEventTest implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("Application Context Closed Event Test!!");
    }
}
