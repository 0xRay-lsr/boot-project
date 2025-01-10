package cn.aps.boot.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : lishirui
 */
@EnableConfigurationProperties({MailProperties.class})
@RequiredArgsConstructor
@SpringBootApplication
@RestController
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }


}
