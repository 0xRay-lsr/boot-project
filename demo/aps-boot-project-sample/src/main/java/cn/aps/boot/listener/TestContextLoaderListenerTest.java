package cn.aps.boot.listener;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * @Description : 对于 Web 应用，你可以使用 ServletContextListener 来在应用上下文关闭时执行一些任务。ServletContextListener 事件通常会在应用关闭之前触发。
 * @Author : lishirui
 * @Date ：2025/1/6 14:26
 */
//@Component
public class TestContextLoaderListenerTest extends ContextLoaderListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("servlet context initialized");
        super.contextInitialized(event);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("servlet context destroyed");
        super.contextDestroyed(event);
    }
}
