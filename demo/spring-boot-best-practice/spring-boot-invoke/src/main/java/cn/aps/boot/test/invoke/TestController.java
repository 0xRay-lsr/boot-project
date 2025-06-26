package cn.aps.boot.test.invoke;

import cn.aps.boot.test.invoke.api.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/2/12 09:42
 */
public class TestController {
    @Autowired
    HttpClient httpClient;

    public void test() {
//        httpClient.post("application.name",)
    }
}
