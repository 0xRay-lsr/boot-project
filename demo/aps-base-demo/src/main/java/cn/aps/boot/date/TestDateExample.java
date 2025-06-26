package cn.aps.boot.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/6/3 20:27
 */
public class TestDateExample {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime yesterday = localDateTime.minusDays(1);
        System.out.println(yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    }
}
