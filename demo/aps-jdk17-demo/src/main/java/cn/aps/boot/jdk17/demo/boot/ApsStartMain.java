package cn.aps.boot.jdk17.demo.boot;

import org.springframework.boot.SpringApplication;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/6/17 16:40
 */
public class ApsStartMain {
    protected static void run(String[] args) {
        SpringApplication app = new SpringApplication(ApsStartMain.class);
        app.addBootstrapRegistryInitializer(new ApsParamsRegistryInitializer());
        app.run(args);

    }
}
