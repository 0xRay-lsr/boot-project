package cn.aps.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * @Description : 配置绑定类
 * @Author : lishirui
 * @Date ：2025/3/28 18:26
 */
@Component
public class ConfigRebinder {
    @Autowired
    private ConfigurableEnvironment environment;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 绑定bean
     * @param prefix bean配置前缀
     * @param beanName bean 名称，一般为类首字母小写
     */
    public Class rebindConfigByBeanName(String prefix,String beanName) {
        // 重新绑定配置到 LsrConfig 实例
        Binder binder = Binder.get(environment);
        Object updateConfigObject = applicationContext.getBean(beanName);
        binder.bind(prefix, Bindable.ofInstance(updateConfigObject));
        return updateConfigObject.getClass();
    }
}
