package cn.aps.boot.api;

import org.springframework.core.Ordered;
/**
 * @Description : 扩展springboot生命周期的-完整启动周期
 * @Author : lishirui
 * @Date ：2024/12/5 11:19
 */
public interface ApplicationLifecycle extends Ordered {

    /**
     * Start the component.
     * <p>This method will execute after the Spring application context is refreshed,
     * which means after the singleton beans have been created.</p>
     */
    void start();

    /**
     * Stop the component.
     * <p>This method will be executed after the Spring ContextClosedEvent has completed.</p>
     */
    void stop();

    /**
     * Return the order value of this component.
     *
     * @return the order value.
     */
    int getOrder();

    /**
     * Check whether this component is currently running.
     * <p>In the case of a container, this will return {@code true} only if <i>all</i>
     * components that apply are currently running.
     * @return whether the component is currently running
     */
    boolean isRunning();
}
