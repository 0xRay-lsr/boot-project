package cn.aps.boot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/4/23 17:50
 */
@RestController
public class TestRestController {
    @Resource
    private RestTemplate restTemplate;
    @GetMapping("/test")
    public void test() throws InterruptedException {
        restTemplate.getForObject("http://127.0.0.1:8099/server",String.class);
        Thread.sleep(10000);
        restTemplate.getForObject("http://127.0.0.1:8099/server",String.class);
    }
}
