package cn.aps.boot.config;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/4/23 17:41
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 连接池的最大连接数
     */
    private int maxTotalConnect = 400;

    /**
     * 同路由的并发数
     * 每个路由的最大连接数,如果只调用一个地址,可以将其设置为最大连接数
     */
    private int maxConnectPerRoute = 200;

    /**
     * 客户端和服务器建立连接超时，默认15s
     * 最大约21秒,因为内部tcp在进行三次握手建立连接时,默认tcp超时时间是20秒
     */
    private int connectTimeout = 15 * 1000;

    /**
     * 指客户端从服务器读取数据包的间隔超时时间,不是总读取时间，默认30s
     */
    private int readTimeout = 30 * 1000;

    /**
     * 从连接池获取连接的超时时间,不宜过长,单位ms
     */
    private int connectionRequestTimout = 15 * 1000;

    private int keepAliveTime = 20 * 1000;
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient());
        return factory;
    }

    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(10);
        connectionManager.setDefaultMaxPerRoute(100);

        // 仅设置为默认值，单次调用可能会重新设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(readTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimout)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy((res,context)-> keepAliveTime)
//                .evictIdleConnections(30, TimeUnit.SECONDS)
                .build();
    }
    @Bean
    public RestTemplate buildRestTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        // 超时控制，将超时时间通过requestFactory传进去
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        return restTemplate;
    }
}
