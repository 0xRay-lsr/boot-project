package cn.aps.boot.aop.agent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

/**
 * @Description : 日志输出
 * @Author : lishirui
 * @Date ：2025/7/9 11:19
 */
public class LogWriter {
    private static final Path logFile;

    static {
        String userDir = System.getProperty("user.dir");
        logFile = Paths.get(userDir, "java-agent.log");
    }

    public static void writeLog(String msg) {
        String line = "[" + LocalDateTime.now() + "] " + msg + System.lineSeparator();
        try {
            Files.write(logFile, line.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("[Agent LogWriter] Failed to write log: " + e.getMessage());
        }
    }
}
