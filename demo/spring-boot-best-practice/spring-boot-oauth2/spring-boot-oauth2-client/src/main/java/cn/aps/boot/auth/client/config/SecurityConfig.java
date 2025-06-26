package cn.aps.boot.auth.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description : 业务服务（资源服务器）安全配置  负责验证传入的 JWT token
 * @Author : lishirui
 * @Date ：2025/6/25 10:12
 */
@Configuration
@EnableWebSecurity // 启用Spring Security Web安全功能
public class SecurityConfig {

    /**
     * 配置资源服务器的 SecurityFilterChain
     *
     * @param http HttpSecurity 对象，用于配置Web安全
     * @return SecurityFilterChain 实例
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/public/**").permitAll() // 公开接口，无需认证
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // /api/admin/** 需要 ADMIN 角色
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN") // /api/user/** 需要 USER 或 ADMIN 角色
                        .anyRequest().authenticated() // 其他所有请求都需要认证 (JWT有效即可)
                )
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(jwt -> {
                            jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());// 使用自定义 converter
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 无状态会话管理，不创建Session
                .csrf(csrf -> csrf.disable()); // 禁用 CSRF，因为是无状态 RESTful API
        return http.build();
    }

    /**
     * 自定义 JwtAuthenticationConverter，用于从 JWT 中提取权限
     * 它会从 "scope" claim 提取 SCOPE_ 权限，
     * 也会从我们自定义的 "roles" claim 中提取 ROLE_ 权限。
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 默认会从 "scope" claim 中提取 "SCOPE_" 前缀的权限
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // 1. 获取默认的 scope 权限 (例如 SCOPE_openid, SCOPE_profile, etc.)
            Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);
            // 2. 从 JWT 的 "roles" claim 中提取自定义角色权限
            Set<GrantedAuthority> customAuthorities = new HashSet<>();
            List<String> rolesClaim = jwt.getClaimAsStringList("roles"); // 获取我们自定义的 "roles" claim
            if (rolesClaim != null) {
                rolesClaim.forEach(role -> customAuthorities.add(new SimpleGrantedAuthority(role))); // 注意：这里直接添加，因为它已经是 "ROLE_USER" 格式了
            }
            // 3. 将两种权限合并
            return Stream.concat(authorities.stream(), customAuthorities.stream())
                    .collect(Collectors.toSet());
        });
        return jwtAuthenticationConverter;
    }
}
