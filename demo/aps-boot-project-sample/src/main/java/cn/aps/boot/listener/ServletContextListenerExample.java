package cn.aps.boot.listener;

import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Description : （仅 Web 应用） 与 Servlet 容器的生命周期相关，通常用于在 Web 应用启动或销毁时执行逻辑,常用于：Web 应用启动时的资源初始化，或销毁时的资源清理。
 * @Author : lishirui
 * @Date ：2025/1/6 14:35
 */
@Component
public class ServletContextListenerExample implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ServletContext Initialized!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletContext Destroyed!");
    }
}
