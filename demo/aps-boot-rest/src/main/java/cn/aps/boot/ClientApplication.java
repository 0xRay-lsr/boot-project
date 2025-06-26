package cn.aps.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/4/23 17:48
 */
@SpringBootApplication(scanBasePackages = {"cn.aps.boot.config","cn.aps"})
public class ClientApplication {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "client");
        SpringApplication.run(ClientApplication.class, args);
    }
}
