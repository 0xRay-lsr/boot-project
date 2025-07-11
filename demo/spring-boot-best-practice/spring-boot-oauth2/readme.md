# 协议流程图解
<img src="协议流程图解.png">

    关键安全设计
    授权码(code)：短暂有效期（默认5分钟），一次性使用
    双重验证：客户端身份(client_secret) + 用户身份(login)双重校验
    PKCE扩展（可选）：防止授权码拦截攻击（您代码中未启用）
    刷新令牌(refresh_token)：长期有效期（默认30天），用于获取新的访问令牌


# 概念
    RegisteredClientRepository: 管理注册客户端（Client）信息的仓库。客户端是向认证服务器请求令牌的第三方应用程序。
    OAuth2AuthorizationService: 负责存储和管理授权请求、授权码、访问令牌等与授权流程相关的数据。
    OAuth2AuthorizationConsentService: 存储用户对客户端授权的同意记录。
    AuthorizationServerSettings: 配置认证服务器自身的各种属性，如颁发者URI、各个端点的路径等。
    TokenSettings: 配置特定客户端生成令牌的属性，如Access Token和Refresh Token的有效期、是否复用Refresh Token等。
    ClientSettings: 配置特定客户端的属性，如是否需要用户授权同意。
    JWKSource: 提供了用于签名 JWT 的 JSON Web Key (JWK)。认证服务器使用私钥签名Access Token (JWT)，资源服务器使用公钥验证Access Token。
    JwtEncoder / JwtDecoder: JWT的编码器和解码器。
    OAuth2TokenGenerator: 负责生成不同类型的令牌（Access Token, Refresh Token）。

# 项目结构概览

## 您提供的代码片段主要集中在以下几个文件：

    AuthorizationServerConfig.java: OAuth2 认证服务器的核心配置，定义了客户端、授权模式、令牌生成等。
    SecurityConfig.java: Spring Security 的通用Web安全配置，负责用户认证（如表单登录）和非OAuth2相关路径的授权。
    JwtConfig.java: 用于生成和管理 JWT 签名所需的 RSA 密钥对。
    CustomOAuth2TokenCustomizer.java: 自定义 Access Token (JWT) 的内容，添加额外的 Claims（如用户角色）。
    CustomUserDetailsService.java (未提供，但根据使用推断): Spring Security 的 UserDetailsService 实现，负责加载用户信息（用户名、密码、权限）。
    PasswordGenerator.java: 一个辅助类，用于生成 BCrypt 加密后的密码。

# 登录
<img src="登录流程图.png">
时序图
<img src="登录时序图.png">

# 用户登录 (Authentication)”流程解析

    调用栈分析:
    您的调用栈显示了典型的 Spring Security Form 登录过程：
    loadUserByUsername:35, CustomUserDetailsService: 这是您的自定义用户服务，负责根据用户名加载用户详情。
    retrieveUser:107, DaoAuthenticationProvider: DaoAuthenticationProvider 是 Spring Security 最常用的认证提供者之一，它使用 UserDetailsService 来获取用户详情。
    authenticate:133, AbstractUserDetailsAuthenticationProvider: DaoAuthenticationProvider 的父类，提供了基于 UserDetails 的认证逻辑。
    authenticate:182, ProviderManager: ProviderManager 是 AuthenticationManager 的默认实现，它会遍历注册的 AuthenticationProvider 列表，直到找到一个支持并成功处理认证请求的提供者。
    attemptAuthentication:85, UsernamePasswordAuthenticationFilter: 这是处理 /login POST 请求的过滤器。它从请求中提取用户名和密码，并创建一个 UsernamePasswordAuthenticationToken，然后将其交给 AuthenticationManager（即 ProviderManager）进行认证。
    doFilter:231, AbstractAuthenticationProcessingFilter: UsernamePasswordAuthenticationFilter 的父类，负责处理认证请求的核心逻辑（包括调用 attemptAuthentication 和处理认证成功/失败）。
    doFilter:221, AbstractAuthenticationProcessingFilter: 同上。
    doFilter:374, FilterChainProxy$VirtualFilterChain: Spring Security 的核心过滤器链代理，它会按顺序调用链中的各个过滤器。
    LogoutFilter: 处理用户登出请求的过滤器。
    CsrfFilter: 处理 CSRF 保护的过滤器。
    CorsFilter: 处理 CORS 跨域请求的过滤器。
    HeaderWriterFilter: 添加各种安全 HTTP 响应头的过滤器。
    SecurityContextHolderFilter: 确保 SecurityContext 在请求开始时被加载并在请求结束时被清除的过滤器。
    从下往上看，整个过程就是典型的 Spring Security 过滤器链的执行顺序，最终由 UsernamePasswordAuthenticationFilter 触发认证流程，并由 DaoAuthenticationProvider 和 CustomUserDetailsService 完成实际的用户凭证验证。

# 获取所有端点
    http://localhost:9000/.well-known/openid-configuration

# 登录
    http://localhost:9000

# 获取token
    http://localhost:9000/oauth2/authorize?response_type=code&client_id=my-client&scope=openid%20profile%20message.read%20message.write%20offline_access&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/my-client

# 获取code
    输入完获取token的地址后，会自动跳转到redirect_uri，在地址栏中可以看到code

# 刷新token
    详细请看 boot.postman_collection.json