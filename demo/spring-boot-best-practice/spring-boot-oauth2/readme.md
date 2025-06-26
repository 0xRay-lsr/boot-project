# 获取所有端点
    http://localhost:9000/.well-known/openid-configuration

# 登录
    http://localhost:9000

# 获取token
    http://localhost:9000/oauth2/authorize?response_type=code&client_id=my-client&scope=openid%20profile%20message.read%20message.write%20offline_access&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/my-client

# 获取code
    输入完获取token的地址后，会自动跳转到redirect_uri，在地址栏中可以看到code

# 刷新token
    