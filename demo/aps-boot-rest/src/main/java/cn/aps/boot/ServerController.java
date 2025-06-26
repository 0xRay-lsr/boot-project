package cn.aps.boot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/4/23 17:51
 */
@RestController
public class ServerController {
    @GetMapping("/server")
    public void server() {
        System.out.println("server");
    }
}
