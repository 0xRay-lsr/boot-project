package cn.aps.boot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description :用于模拟测试arthas能力
 * @Author : lishirui
 * @Date ：2024/11/20 15:20
 */
@RestController
@RequestMapping("/arthas")
public class ArthasTestController {
    public static final Logger logger = LoggerFactory.getLogger(ArthasTestController.class);
    private boolean isProcess = true;

    /**
     * trace：跟踪方法调用，输出执行耗时 ： trace cn.aps.boot.controller.ArthasTestController calculate
     * time：统计方法执行的耗时分布：time cn.aps.boot.controller.ArthasTestController calculate
     *
     * @throws InterruptedException
     */
    @GetMapping("/profileStart")
    public void profileStart() throws InterruptedException {
        logger.info("profile start .....");
        while (isProcess) {
            calculate();
            Thread.sleep(2000); // 模拟间隔
        }
        logger.info("profile end .....");
    }

    public int calculate() {
        try {
            Thread.sleep((long) (Math.random() * 1000)); // 模拟耗时
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (int) (Math.random() * 100);
    }

    @GetMapping("/profileEnd")
    public void profileEnd() throws InterruptedException {
        isProcess = false;
    }

    /**
     * 监控方法的返回值：watch cn.aps.boot.controller.ArthasTestController register returnObj
     * 统计方法调用次数和性能数据：monitor cn.aps.boot.controller.ArthasTestController register
     */
    @GetMapping("/method")
    public void UserInfo() {
        logger.info("method start .....");
        register("zhangsan", "12345");
        register("lisi", "admin");
        logger.info("method end .....");
    }

    public String register(String username, String password) {
        if (password.length() < 6) {
            return "Password too short!";
        }
        return "User " + username + " registered successfully.";
    }

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    /**
     * 查看线程状态 thread
     * thread [线程ID]
     */
    @GetMapping("/thread")
    public void thread() {
        logger.info("thread start .....");
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    logger.info("Thread 1 acquired lock2");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                synchronized (lock1) {
                    logger.info("Thread 2 acquired lock2");
                }
            }
        });

        t1.start();
        t2.start();
    }

    /**
     * 反编译类：jad cn.aps.boot.controller.ArthasTestController
     * 重新加载：redefine /path/to/modified/ArthasTestController.class
     *
     * @return
     */
    @GetMapping("editClass")
    public String editClass() {
        return "当前类为ArthasTestController";
    }


}
