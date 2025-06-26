package cn.aps.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/3/28 18:07
 */
@Configuration
@ConfigurationProperties(prefix = "lsr")
public class LsrConfig {
    private String test;
    private String test2;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getTest2() {
        return test2;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }
}
