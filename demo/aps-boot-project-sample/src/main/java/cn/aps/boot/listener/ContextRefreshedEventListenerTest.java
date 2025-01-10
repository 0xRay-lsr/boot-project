package cn.aps.boot.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Description : 表示 Spring 上下文被刷新，通常发生在应用启动时。
 * @Author : lishirui
 * @Date ：2025/1/6 14:27
 */
@Component
public class ContextRefreshedEventListenerTest implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("Spring Context Refreshed!");
    }
}
