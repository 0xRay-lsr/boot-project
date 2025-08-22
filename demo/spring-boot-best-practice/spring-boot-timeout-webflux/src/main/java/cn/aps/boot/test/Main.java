package cn.aps.boot.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring Boot 主启动类
 *
 * @author lishirui
 * @date 2025-08-21
 */
@SpringBootApplication
@EnableTransactionManagement
public class Main {
    /**
     * 正常请求
     * http://localhost:8080/api/fast
     * 线程中断模拟
     * http://localhost:8080/api/slow-but-interruptible
     * 模拟业务耗时-超时
     * http://localhost:8080/api/infinite-loop
     * 数据库事务回滚
     * http://localhost:8080/api/transactional-timeout
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}