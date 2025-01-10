package cn.aps.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.aps"})
public class BootApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(BootApplication.class);
            System.out.println("SystemStatus:Success");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}