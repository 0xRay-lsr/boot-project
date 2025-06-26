//package cn.aps.boot.invoke;
//
//import feign.Client;
//import feign.Feign;
//import feign.httpclient.ApacheHttpClient;
//import org.apache.http.client.HttpClient;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
///**
// * @Description :调用组件配置
// * @Author : lishirui
// * @Date ：2025/2/11 17:17
// */
//@Configuration
//public class InvokeConfiguration {
//    // 声明CloseableHttpClient Bean
//    @Bean
//    public CloseableHttpClient httpClient() {
//        return HttpClientBuilder.create()
//                .setMaxConnTotal(100)    // 最大连接数
//                .setMaxConnPerRoute(20)  // 单路由最大连接数
//                .build();
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "invoke.type", ignoreInvalidFields = true)
//    public Feign.Builder feignBuilder() {
//        return Feign.builder(httpClient());
//    }
//
//
//    @Bean
//    public RestTemplate restTemplate(CloseableHttpClient httpClient) {
//        return new RestTemplate(
//                new HttpComponentsClientHttpRequestFactory(httpClient)
//        );
//    }
//
//    @Bean
//    public Client feignClient(HttpClient httpClient) {
//        //
//
//        return new ApacheHttpClient(httpClient);
//    }
//
//    //
//}
