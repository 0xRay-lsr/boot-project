package cn.aps.boot.auth.server;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Description : 明文密码转换成spring security 加密后的密码
 * @Author : lishirui
 * @Date ：2025/6/25 11:07
 */
public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "lishirui"; // 你想要设置的明文密码
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded password for '" + rawPassword + "': " + encodedPassword);
        // 复制这个输出到你的数据库中对应用户的password字段
    }
}
