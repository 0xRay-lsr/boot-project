package cn.aps.boot.auth.server.config;

import cn.aps.boot.auth.server.user.CustomUserDetailsService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @Description :  Spring Security Web 安全配置 负责用户认证和授权，以及Form登录页面的配置
 * @Author : lishirui
 * @Date ：2025/6/23 16:52
 */
@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    @Resource
    CustomUserDetailsService customUserDetailsService;

    /**
     * 配置密码编码器
     * Spring Security 5 之后要求必须使用 PasswordEncoder
     * 这里使用 BCryptPasswordEncoder，它是一种安全的密码哈希算法
     *
     * @return PasswordEncoder 实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // !!! 注意：在 data.sql 中我们使用了 {noop} 前缀，这里为了演示方便，
        //      实际生产环境强烈建议将数据库中的密码进行BCrypt加密，并去掉 {noop}。
        //      如：new BCryptPasswordEncoder().encode("password") 生成的加密字符串。
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 配置默认的 Spring Security 过滤链
     * Order(2) 表示优先级低于 AuthorizationServerConfig 中的过滤链
     * 主要用于：
     * 1. 认证用户的 `/login` 页面
     * 2. 保护除了 OAuth2 授权端点之外的其他所有请求（如果需要）
     *
     * @param http HttpSecurity 对象，用于配置Web安全
     * @return SecurityFilterChain 实例
     * @throws Exception 配置异常
     */
    @Bean
    @Order(2) // 低于 AuthorizationServerConfig 的 Order(1)，确保通用规则最后匹配
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // 配置授权规则
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/login")).permitAll()
                                .anyRequest().authenticated() // 所有其他请求都需要认证
                )
                // 配置表单登录
                .formLogin(Customizer.withDefaults()) // 启用基于表单的登录，提供默认登录页面
                // 配置用户详情服务
                .userDetailsService(customUserDetailsService)
                // 配置 CSRF 保护 (H2 Console 除外)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                )
                // 配置 Headers (H2 Console 的 frameOptions)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // 允许同源 iframe 访问 H2 Console
                );
        return http.build();
    }
}
