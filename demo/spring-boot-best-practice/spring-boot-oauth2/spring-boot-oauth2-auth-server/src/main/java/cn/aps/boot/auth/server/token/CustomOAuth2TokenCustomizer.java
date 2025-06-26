package cn.aps.boot.auth.server.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description : 自定义 JWT Token 的内容，将用户角色添加到 JWT Claims 中
 * @Author : lishirui
 * @Date ：2025/6/26 10:00
 */
public class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    @Override
    public void customize(JwtEncodingContext context) {
        // 只对 Access Token 进行定制
        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            Authentication principal = context.getPrincipal(); // 获取当前认证的 Principal (用户)
            if (principal != null) {
                // 提取用户的权限 (GrantedAuthority)，通常由 UserDetailsService 提供
                Set<String> authorities = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                // 将权限添加到 JWT 的 "authorities" claim 中
                // 你可以选择一个自定义的 claim 名称，例如 "roles" 或 "authorities"
                // 资源服务器会根据这个 claim 来提取权限
                context.getClaims().claims(claims -> {
                    // Spring Security 默认会将 "scope" claim 映射为 SCOPE_ 前缀的权限
                    // 这里我们添加一个名为 "roles" 的 claim 来存放 ROLE_ 前缀的权限
                    Set<String> roles = authorities.stream().filter(authority -> authority.startsWith("ROLE_")).collect(Collectors.toSet());
                    if (!roles.isEmpty()) {
                        claims.put("roles", roles); // 将角色列表添加到 JWT 的 "roles" claim
                    }
                    // 你也可以选择将所有权限（包括 SCOPE_ 和 ROLE_）都放入一个 claim
                    // 但通常建议区分，或在资源服务器端统一处理
                    // claims.put("authorities", authorities);
                });
            }
        }
    }
}
