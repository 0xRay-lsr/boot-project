package cn.aps.boot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CamundaApplication {
    public static void main(String[] args) {
        SpringApplication.run(CamundaApplication.class, args);
        System.out.println("====================CamundaApplication启动完成====================");
    }
}