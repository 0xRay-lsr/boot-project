package cn.aps.boot.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description : 表示 Spring Boot 应用启动完成并准备好接受请求时触发。通常用于执行初始化完成后的操作。
 * @Author : lishirui
 * @Date ：2025/1/6 14:28
 */
@Component
public class ApplicationReadyEventListenerTest implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Spring Boot Application is ready!");
    }
}
