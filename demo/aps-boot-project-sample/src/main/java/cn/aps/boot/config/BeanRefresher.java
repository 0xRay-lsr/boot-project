package cn.aps.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Description : 配置刷新类
 * @Author : lishirui
 * @Date ：2025/3/28 18:28
 */
@Component
public class BeanRefresher {
    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * 刷新指定bean
     * @param beanName beanName 一般为类首字母小写
     * @param bean 具体的bean实例
     */
    public void refreshBean(String beanName,Object bean) {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getBeanFactory();
        // 销毁原有 Bean
        if (context.containsBean(beanName)) {
            context.getBeanFactory().destroyBean(beanName);
            registry.removeBeanDefinition(beanName);
        }
        // 重新注册并初始化 Bean
        ((DefaultListableBeanFactory) context.getBeanFactory()).registerBeanDefinition(beanName,
                BeanDefinitionBuilder.genericBeanDefinition((Class<?>) bean).getBeanDefinition());
        context.getBean(beanName); // 触发初始化
    }
}
