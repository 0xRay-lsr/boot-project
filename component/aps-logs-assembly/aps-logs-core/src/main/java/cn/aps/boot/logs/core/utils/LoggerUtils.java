package cn.aps.boot.logs.core.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * @Description : 日志操作工具类
 * @Author : lishirui
 * @Date ：2025/4/8 16:31
 */
public class LoggerUtils {

    /**
     * @param loggerName
     * @param level      public static final Level OFF;
     *                   public static final Level FATAL;
     *                   public static final Level ERROR;
     *                   public static final Level WARN;
     *                   public static final Level INFO;
     *                   public static final Level DEBUG;
     *                   public static final Level TRACE;
     *                   public static final Level ALL;
     */
    public void setLogLevel(String loggerName, String level) {
        if (isLog4j2Available()) {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            Logger logger = context.getLogger(loggerName);
            logger.setLevel(Level.valueOf(level.toLowerCase()));
        } else if (isLogbackAvailable()) {
            ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerName);
            logger.setLevel(ch.qos.logback.classic.Level.valueOf(level.toLowerCase()));
        }
    }

    public static boolean isLog4j2Available() {
        try {
            // 尝试加载 Log4j2 的相关类
            Class.forName("org.apache.logging.log4j.core.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isLogbackAvailable() {
        try {
            // 尝试加载 Logback 的相关类
            Class.forName("ch.qos.logback.classic.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
