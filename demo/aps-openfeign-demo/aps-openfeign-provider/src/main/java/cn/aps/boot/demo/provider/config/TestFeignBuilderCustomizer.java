package cn.aps.boot.demo.provider.config;

import feign.Feign;
import feign.Logger;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.stereotype.Component;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/2/18 10:44
 */
@Component
public class TestFeignBuilderCustomizer implements FeignBuilderCustomizer {
    @Override
    public void customize(Feign.Builder builder) {
        builder.requestInterceptor(new TestFeignRequestInterceptor());
    }
}
