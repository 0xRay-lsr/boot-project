package cn.aps.boot.test.config;

import cn.aps.boot.test.handler.TimeoutWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * WebFlux配置类，用于注册自定义的WebExceptionHandler。
 * @author lishirui
 * @date 2025-08-21
 */
@Configuration
public class WebConfig implements WebFluxConfigurer {

    // 显式注册我们的WebExceptionHandler
    @Bean
    @Order(-1) // 确保它在默认的ErrorWebExceptionHandler之前执行
    public TimeoutWebExceptionHandler timeoutWebExceptionHandler() {
        return new TimeoutWebExceptionHandler();
    }
}