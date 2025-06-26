package cn.aps.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 动态更新配置的值--spring容器中的
 * @Author : lishirui
 * @Date ：2025/3/28 18:20
 */
@Component
public class DynamicConfigUpdater {
    @Autowired
    private ConfigurableEnvironment environment;

    public void updateConfig(String key, String value ,String beanName) {
        Map<String, Object> newConfig = new HashMap<>();
        newConfig.put(key, value);
        // 创建新属性源并覆盖旧值
        MapPropertySource source = new MapPropertySource(beanName, newConfig);
        environment.getPropertySources().addFirst(source);
    }
}
