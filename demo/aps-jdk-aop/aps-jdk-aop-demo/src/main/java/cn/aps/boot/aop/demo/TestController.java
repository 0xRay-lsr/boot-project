package cn.aps.boot.aop.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description : 测试待增强的controller
 * @Author : lishirui
 * @Date ：2025/7/10 15:15
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/hello")
    public String sayHello(@RequestBody String name) {
        System.out.println("Hello, " + name);
        return "Hello, " + name;
    }

    @PostMapping("/insert")
    public String insert(@RequestBody String name) {
        System.out.println("insert, " + name);
        return "insert, " + name;
    }
}
