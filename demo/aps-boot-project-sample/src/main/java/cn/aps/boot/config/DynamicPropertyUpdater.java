package cn.aps.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description : 通过配置文件前缀获取对应的bean
 * @Author : lishirui
 * @Date ：2025/3/31 11:06
 */
@Component
public class DynamicPropertyUpdater {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DynamicConfigUpdater dynamicConfigUpdater;

    @Autowired
    private ConfigRebinder configRebinder;

    @Autowired
    private BeanRefresher beanRefresher;

    // 动态查找和更新配置项
    public void updateProperty(String propertiesPrefix, String propertyKey, String newValue) {
        // 获取所有注册的bean
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ConfigurationProperties.class);
        for (Object bean : beans.values()) {
            // 获取该bean的配置前缀
            ConfigurationProperties configProperties = AnnotationUtils.findAnnotation(bean.getClass(), ConfigurationProperties.class);
            if (configProperties != null && configProperties.prefix().equals(propertiesPrefix)) {
                // 获取类的全名称（包含CGLIB的后缀）
                String className = bean.getClass().getSimpleName();
                // 使用正则表达式去除代理生成的部分（如：$$EnhancerBySpringCGLIB$$xxx）
                String realClassName = className.replaceAll("\\$\\$.*", "");
                // 将首字母小写
                String beanName = toLowerCaseFirstLetter(realClassName);
                // 找到匹配的类（以 "lsr" 为前缀的配置类）
                updateBeanProperty(propertiesPrefix, propertyKey, newValue, beanName);
                // 重新绑定配置到 Bean
                Class updateBean = configRebinder.rebindConfigByBeanName(propertiesPrefix, beanName);
                // 强制刷新条件注解的 Bean
                beanRefresher.refreshBean(beanName, updateBean);
                break;
            }
        }
    }

    // 使用反射查找并更新bean的属性
    private void updateBeanProperty(String propertiesPrefix, String propertyKey, String newValue, String beanName) {
        dynamicConfigUpdater.updateConfig(propertiesPrefix + "." + propertyKey, newValue, beanName);
    }

    // 将字符串的首字母转换为小写，并保留后续字符的原始大小写
    public static String toLowerCaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // 将首字母小写并返回新的字符串
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
