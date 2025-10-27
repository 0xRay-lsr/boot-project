/**n * @author lishiruin * @date 2025-10-27n */
package cn.aps.boot.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;

/**
 * @Author : lishirui
 */
@SpringBootApplication
@ComponentScan(basePackages = {"cn.aps.boot.test.controller", "cn.aps.boot.test.statemachine", "cn.aps.boot.test.service"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
