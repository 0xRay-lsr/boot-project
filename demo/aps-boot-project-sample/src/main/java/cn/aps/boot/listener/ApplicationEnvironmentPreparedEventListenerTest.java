package cn.aps.boot.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * @Description : 在 Spring Boot 应用环境准备阶段触发。此时，Spring 环境已经准备好，可以访问配置属性，但 Spring 上下文尚未创建。
 * @Author : lishirui
 * @Date ：2025/1/6 14:29
 */
@Component
public class ApplicationEnvironmentPreparedEventListenerTest implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        System.out.println("Environment Prepared: " + environment.getProperty("spring.application.name"));
    }
}
