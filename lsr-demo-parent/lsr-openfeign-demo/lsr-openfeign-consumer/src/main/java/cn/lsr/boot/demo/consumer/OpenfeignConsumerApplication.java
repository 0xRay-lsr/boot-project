package cn.lsr.boot.demo.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2024/7/23 10:23
 */

@EnableDiscoveryClient
@EnableFeignClients  //扫描和注册feign客户端的beanDefinition
@SpringBootApplication(scanBasePackages = "cn.lsr")
public class OpenfeignConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenfeignConsumerApplication.class,args);
        System.out.println("success");
    }
}
