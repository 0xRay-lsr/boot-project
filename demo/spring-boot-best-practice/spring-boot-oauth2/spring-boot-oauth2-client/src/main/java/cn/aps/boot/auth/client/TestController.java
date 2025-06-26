package cn.aps.boot.auth.client;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/6/18 17:23
 */
@RestController
public class TestController {
    /**
     * 公开接口，无需认证即可访问 http://127.0.0.1:8081/public/hello
     *
     * @return 欢迎信息
     */
    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello from Public API!";
    }

    /**
     * 需要有效 JWT 的接口 http://127.0.0.1:8081/api/user/me
     * 通过 @AuthenticationPrincipal 注解可以直接获取解析后的 Jwt 对象
     *
     * @param jwt JWT 对象
     * @return 用户信息 DTO
     */
    @GetMapping("/api/user/me")
        public UserInfo getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        return new UserInfo(
                jwt.getSubject(), // JWT 的 subject 字段通常是用户名
                jwt.getClaims().get("scope").toString(), // 获取 scope 字段（权限范围）
                jwt.getClaims().get("aud").toString() // 获取 aud 字段（受众）
        );
    }

    /**
     * 需要 'USER' 或 'ADMIN' 角色才能访问的接口
     * 通过 Authentication 对象获取当前认证用户的信息和权限
     *
     * @param authentication 认证对象
     * @return 认证信息
     */
    @GetMapping("/api/user/data")
    public String getUserData(Authentication authentication) {
        String username = authentication.getName(); // 获取用户名
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); // 获取权限列表
        return "Hello User " + username + "! Your roles: " + roles + ". This is user data.";
    }

    /**
     * 需要 'ADMIN' 角色才能访问的接口
     *
     * @param authentication 认证对象
     * @return 认证信息
     */
    @GetMapping("/api/admin/data")
    public String getAdminData(Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return "Hello Admin " + username + "! Your roles: " + roles + ". This is admin data.";
    }
}
