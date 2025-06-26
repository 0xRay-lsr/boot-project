package cn.aps.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description : 测试配置刷新端点
 * @Author : lishirui
 * @Date ：2025/3/28 18:30
 */
@RestController
public class RefreshController {
    @Autowired
    DynamicPropertyUpdater dynamicPropertyUpdater;
    @Autowired
    YmlFileUpdater ymlFileUpdater;
    @Autowired
    LsrConfig lsrConfig;

    /**
     * http://127.0.0.1:8066/refreshConfig?propertiesPrefix=lsr&propertyKey=test&newValue=fff
     *
     * @param propertiesPrefix 配置前缀 如：配置类的@ConfigurationProperties(prefix = "lsr")
     * @param propertyKey      配置key，具体要修改的属性名称
     * @param newValue         新的值
     * @return
     * @throws Exception
     */
    @GetMapping("/refreshConfig")
    public String refreshLsrConfig(String propertiesPrefix, String propertyKey, String newValue) throws Exception {
        // 更新配置
        dynamicPropertyUpdater.updateProperty(propertiesPrefix, propertyKey, newValue);
        // 更新配置文件，不可以更新本地项目中的文件，因为springboot启动，会认为是静态文件，实际测试请通过
        // java -jar myapp.jar --spring.config.location=file:/opt/config/application.yml 启动项目进行测试
        // java -jar aps-boot-project-sample-1.0-SNAPSHOT.jar --spring.config.location=file:/Users/lsr/github/aps-framework/demo/aps-boot-project-sample/target
        ymlFileUpdater.updateYamlFile(propertiesPrefix + "." + propertyKey, newValue);
        return "配置刷新成功！配置key为：" + propertiesPrefix + "." + propertyKey + ",当前值: " + newValue;
    }

    /**
     * 用于测试值是否发生变化
     * http://127.0.0.1:8066/testConfig
     */
    @GetMapping("/testConfig")
    public void getConfig() {
        System.out.println(lsrConfig.getTest());
    }

}