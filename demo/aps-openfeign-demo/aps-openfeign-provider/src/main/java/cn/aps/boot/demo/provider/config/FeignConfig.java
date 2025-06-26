package cn.aps.boot.demo.provider.config;

import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/2/18 09:39
 */
@Configuration
public class FeignConfig {
//    @Bean
    public RequestInterceptor requestInterceptor() {
        return new TestFeignRequestInterceptor();
    }

    @Bean
    @Primary
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .requestInterceptor(requestInterceptor()) // 添加自定义拦截器
                .logger(new Logger.JavaLogger().appendToFile("feign.log")) // 打印日志到文件
                .logLevel(Logger.Level.FULL); // 设置日志级别
    }
}
