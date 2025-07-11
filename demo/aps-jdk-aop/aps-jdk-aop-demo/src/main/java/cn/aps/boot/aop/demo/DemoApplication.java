package cn.aps.boot.aop.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description : demo 启动类
 * @Author : lishirui
 * @Date ：2025/7/10 15:15
 */
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("start success");
    }
}
