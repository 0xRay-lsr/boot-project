package cn.aps.boot.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description : oauth2 认证服务启动类
 * @Author : lishirui
 * @Date ：2025/6/18 17:18
 */
@SpringBootApplication(scanBasePackages = "cn.aps")
public class OauthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OauthServerApplication.class, args);
    }
}
