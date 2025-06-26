package cn.aps.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.Objects;

/**
 * @Description : yml文件更新
 * @Author : lishirui
 * @Date ：2025/3/31 10:54
 */
@Component
public class YmlFileUpdater {
    @Autowired
    private Environment environment;


    /**
     * 根据key，value 更新yml文件
     *
     * @param key
     * @param value
     * @throws IOException
     */
    public void updateYamlFile(String key, String value) {
        // --spring.config.location=/Users/lsr/github/aps-framework/demo/aps-boot-project-sample/src/main/resources/application.yml
        String configLocation = environment.getProperty("spring.config.location");
        if (configLocation != null) {
            // 去掉 "file:" 前缀，获取本地文件路径
            configLocation = Objects.requireNonNull(configLocation).replace("file:", "");
        }
        File yamlFile = new File(configLocation);
        if (!yamlFile.exists()) {
            System.out.println("YML 文件不存在：" + configLocation);
            return;
        }
        FileInputStream inputStream = null;
        FileWriter writer = null;
        try {
            inputStream = new FileInputStream(yamlFile);
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);
            updateMap(data, key, value);
            writer = new FileWriter(yamlFile);
            DumperOptions options = new DumperOptions();
            options.setIndent(2);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yamlOutput = new Yaml(options);
            yamlOutput.dump(data, writer);
            System.out.println("YML 文件更新成功：" + configLocation);
        } catch (IOException e) {
            System.out.println("更新配置文件失败：" + e.getMessage());
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (writer != null) writer.close();
            } catch (IOException e) {
                System.out.println("关闭流异常：" + e.getMessage());
            }
        }
    }

    private static void updateMap(Map<String, Object> data, String key, String value) {
        String[] keys = key.split("\\.");
        Map<String, Object> currentMap = data;
        for (int i = 0; i < keys.length - 1; i++) {
            currentMap = (Map<String, Object>) currentMap.get(keys[i]);
        }
        currentMap.put(keys[keys.length - 1], value);
    }

}
