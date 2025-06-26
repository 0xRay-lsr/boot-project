package cn.aps.boot.jdk17.demo.boot.listener;

import org.springframework.boot.ConfigurableBootstrapContext;

/**
 * @Description :启动前扩展
 * @Author : lishirui
 * @Date ：2025/6/17 20:24
 */
public interface ApplicationInitPoint {
    void init(ConfigurableBootstrapContext bootstrapContext);
}
