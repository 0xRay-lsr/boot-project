package cn.aps.boot.auth.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/6/25 10:10
 */
@RestController
public class TestController {
    /**
     * 根路径测试接口
     * @return 欢迎信息
     */
    @GetMapping("/")
    public String index() {
        return "Welcome to the Authorization Server!";
    }
    /**
     * 登录后可访问的测试接口
     * @return 用户信息
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Auth Server after login!";
    }
}
