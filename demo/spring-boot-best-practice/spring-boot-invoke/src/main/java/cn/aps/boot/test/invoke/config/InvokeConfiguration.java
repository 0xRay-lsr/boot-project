package cn.aps.boot.test.invoke.config;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Description :调用组件配置
 * @Author : lishirui
 * @Date ：2025/2/11 17:17
 */
@Configuration
public class InvokeConfiguration {

    @Bean
    public HttpClient sharedHttpClient() {
        return HttpClientBuilder.create()
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(50)
                .build();
    }


    @Bean
    public Client feignClient(HttpClient httpClient) {
        return new ApacheHttpClient(httpClient);
    }
}
