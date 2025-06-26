package cn.aps.boot.auth.server.config;

import cn.aps.boot.auth.server.token.CustomOAuth2TokenCustomizer;
import cn.aps.boot.auth.server.user.CustomUserDetailsService;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.time.Duration;

/**
 * @Description : OAuth2 认证服务器核心配置 定义了客户端、授权模式、令牌生成等
 * @Author : lishirui
 * @Date ：2025/6/23 16:56
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {
    @Resource // 注入你的 CustomUserDetailsService
    CustomUserDetailsService customUserDetailsService;

    /**
     * 配置 OAuth2 Authorization Server 过滤链
     * Order(1) 表示最高优先级，先于 SecurityConfig 中的默认过滤链执行
     * 负责处理所有 OAuth2 相关的端点请求 (如 /oauth2/authorize, /oauth2/token, /oauth2/jwks等)
     *
     * @param http HttpSecurity 对象
     * @return SecurityFilterChain 实例
     * @throws Exception 配置异常
     */
    @Bean
    @Order(1) // 优先级最高，确保 OAuth2 流量先经过此链
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // 1. 应用默认的 OAuth2 Authorization Server 配置。
        //    这个方法内部会配置 /oauth2/** 相关的安全规则，并包含一个针对这些路径的兜底 anyRequest().authenticated()。
        //    它还会处理未认证时的重定向（默认是 /login）。
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // 2. 获取 OAuth2 Authorization Server 配置器并进行定制 (例如启用 OIDC)。
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults()); // 启用 OpenID Connect 1.0
        // 3. 配置未认证时的异常处理，重定向到 /login
        //    这是 Authorization Server 自身未认证时（例如访问 /oauth2/authorize 但未登录）的行为
        http.exceptionHandling(exceptions ->
                exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        );
        // 4. 配置 JWT 验证作为资源服务器（如果这个应用也作为资源服务器）
        http.oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(Customizer.withDefaults()));
        // **重要：不在这里添加任何 anyRequest().authenticated() 或针对非OAuth2路径的 authorizeHttpRequests**
        // **H2 Console 和普通的 /login 路径将由另一个 SecurityFilterChain 处理**
        return http.build();
    }

    /**
     * 注册客户端信息
     * 客户端是向认证服务器请求令牌的应用（如前端应用、其他微服务）
     *
     * @return RegisteredClientRepository 实例
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        // 构建一个注册客户端
        RegisteredClient registeredClient = RegisteredClient.withId("my-client") // 这里的id 在获取token时会用到
                .clientId("my-client") // 客户端标识   客户端访问请求头需要携带 my-client:secret 加密后的值进行认证 请求头参数 Authorization: Basic bXlzdHJlY2FzOnNlY3JldA==
                .clientSecret("{noop}secret") // 客户端密钥，{noop}secret表示不编码，生产环境请加密
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // 客户端认证方式：HTTP Basic
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // 授权码模式
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // 刷新令牌模式
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // 客户端凭证模式 (适合机器间通信)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/my-client") // 回调URI，授权成功后重定向到此URI
                .scope(OidcScopes.OPENID) // OpenID Connect 范围
                .scope(OidcScopes.PROFILE) // OpenID Connect 范围
                .scope("message.read") // 自定义范围，用于业务服务
                .scope("message.write")
                .scope("offline_access") //刷新令牌范围
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build()) // 授权码模式需要用户确认授权提供一个确认页面
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(5)) // Access Token 有效期 5 分钟
                        .refreshTokenTimeToLive(Duration.ofHours(1)) // Refresh Token 有效期 1 小时
                        .reuseRefreshTokens(false) // Refresh Token 不可重复使用，每次刷新生成新的
                        .build())
                .build();
        // 将客户端注册到内存库中，生产环境应使用 JdbcRegisteredClientRepository 存储到数据库
        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    /**
     * 定义一个 JwtEncoder Bean，Spring Security Authorization Server 默认会生成一个。
     * 通常无需手动创建，但为了在 tokenGenerator 中使用它，可以将其声明为一个 Bean。
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new org.springframework.security.oauth2.jwt.NimbusJwtEncoder(jwkSource);
    }

    /**
     * 自定义 OAuth2TokenGenerator Bean，用于将自定义的 OAuth2TokenCustomizer 应用到令牌生成过程中。
     * 这个方法会创建 JwtGenerator 并将其与你的 customOAuth2TokenCustomizer 关联。
     */
    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JwtEncoder jwtEncoder) {
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(customOAuth2TokenCustomizer()); // 将你的定制器设置给 JwtGenerator
        // 添加 OAuth2RefreshTokenGenerator 以便生成 Refresh Token
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, refreshTokenGenerator);
    }

    @Bean
    public CustomOAuth2TokenCustomizer customOAuth2TokenCustomizer() {
        return new CustomOAuth2TokenCustomizer();
    }

    /**
     * OAuth2 授权服务
     * 用于存储和管理授权请求、授权码、访问令牌等信息
     * 生产环境应使用 JdbcOAuth2AuthorizationService 存储到数据库
     *
     * @return OAuth2AuthorizationService 实例
     */
    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    /**
     * OAuth2 授权同意服务
     * 用于存储和管理用户对客户端授权的同意记录
     * 生产环境应使用 JdbcOAuth2AuthorizationConsentService 存储到数据库
     *
     * @return OAuth2AuthorizationConsentService 实例
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }

    /**
     * JWT 解码器
     * 用于验证和解码 JWT Token。由 JWKSource 提供公钥进行验证。
     *
     * @param jwkSource JWT 密钥源
     * @return JwtDecoder 实例
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 认证服务器设置
     * 定义认证服务器的各种端点 URI
     * issuer-uri 必须与 application.yml 中的配置一致
     *
     * @return AuthorizationServerSettings 实例
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:9000") // 颁发者 URI，与 application.yml 保持一致
                .build();
    }
}
