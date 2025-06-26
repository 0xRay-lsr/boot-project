package cn.aps.boot.test.invoke.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/2/11 17:18
 */
@ConfigurationProperties
public class HttpClientProperties {
    private int connectTimeout = 1000;
    private int readTimeout = 5000;
    private int maxRetries = 3;
    // Getters and Setters...

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
}
