package cn.aps.boot.web.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import java.time.Duration;

/**
 * 自定义 Web Server
 * @Author : lishirui
 */
//@Component
public class CustomTomcatWebServerFactoryCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory server) {
        server.addConnectorCustomizers((connector) -> {
            connector.setPort(8088);
            connector.setAsyncTimeout(Duration.ofSeconds(20).toMillis());
        });
    }

}