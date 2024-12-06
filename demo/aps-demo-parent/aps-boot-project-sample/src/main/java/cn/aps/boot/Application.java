package cn.aps.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.lsr"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        System.out.println("SystemStatus:Success");
    }
}