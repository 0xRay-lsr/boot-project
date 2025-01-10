package cn.aps.boot.jasypt;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 来源@Author : lishirui
 * 作者：栈长
 */
@Slf4j
@SpringBootTest
public class JasyptTest {

    @Autowired
    private StringEncryptor stringEncryptor;

    /**
     * 来源@Author : lishirui
     * 作者：栈长
     */
    @Test
    public void encrypt() {
        String usernameEnc = stringEncryptor.encrypt("javastack");
        String passwordEnc = stringEncryptor.encrypt("javastack.cn");

        log.info("test username encrypt is {}", usernameEnc);
        log.info("test password encrypt is {}", passwordEnc);

        log.info("test username is {}", stringEncryptor.decrypt(usernameEnc));
        log.info("test password is {}", stringEncryptor.decrypt(passwordEnc));
    }
}