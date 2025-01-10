package cn.aps.boot.admin.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 作者：栈长
 * 来源@Author : lishirui
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void task() {
        log.info("微信公众号Java技术栈正在执行计划任务。。。");
    }


}
