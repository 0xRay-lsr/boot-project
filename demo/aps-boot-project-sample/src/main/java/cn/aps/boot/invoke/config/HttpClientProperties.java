package cn.aps.boot.invoke.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/2/15 11:09
 */
@Configuration
@ConfigurationProperties(prefix = "http.pool")
public class HttpClientProperties {
    private Integer maxTotal = 200;
    private Integer defaultMaxPerRoute = 50;

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }
}
