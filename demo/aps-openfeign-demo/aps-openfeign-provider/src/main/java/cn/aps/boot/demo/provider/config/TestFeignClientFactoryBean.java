package cn.aps.boot.demo.provider.config;

import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/2/18 10:38
 */
//@Component
public class TestFeignClientFactoryBean extends FeignClientFactoryBean {
    public TestFeignClientFactoryBean() {
    }

    @Override
    protected <T> Map<String, T> getInheritedAwareInstances(FeignContext context, Class<T> type) {
        return super.getInheritedAwareInstances(context, type);
    }
}
