package cn.aps.boot.listener;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description : 该事件在 Spring 上下文准备完毕但还未启动时发布，通常用于配置或调整上下文。
 * @Author : lishirui
 * @Date ：2025/1/6 14:34
 */
@Component
public class ApplicationPreparedEventListenerTest implements ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        System.out.println("Spring Boot Application Prepared!");
    }
}
