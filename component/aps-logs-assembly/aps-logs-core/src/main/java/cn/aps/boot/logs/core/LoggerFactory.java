package cn.aps.boot.logs.core;

import cn.aps.boot.logs.api.Logger;
import cn.aps.boot.logs.api.constants.LogCategoryConstant;

/**
 * 日志工厂类,用于获取不同类型的日志记录器
 *
 * @author lishirui
 * @date 2025/3/27 16:54
 */
public class LoggerFactory {

    /**
     * 获取指定类型的Logger
     *
     * @param category 日志类别前缀
     * @param clazz    类
     * @return Logger实例
     */
    private static Logger getCategoryLogger(String category, Class<?> clazz) {
        return getLogger(category + "." + clazz.getName());
    }

    /**
     * 获取启动日志Logger
     */
    public static Logger getBootLogger(Class<?> clazz) {
        return getCategoryLogger(LogCategoryConstant.APS_BOOT, clazz);
    }

    /**
     * 获取应用日志Logger
     */
    public static Logger getAppLogger(Class<?> clazz) {
        return getCategoryLogger(LogCategoryConstant.APS_APP, clazz);
    }

    /**
     * 获取定时任务日志Logger
     */
    public static Logger getTimerLogger(Class<?> clazz) {
        return getCategoryLogger(LogCategoryConstant.APS_TIMER, clazz);
    }

    /**
     * 获取中间件日志Logger
     */
    public static Logger getMidwareLogger(Class<?> clazz) {
        return getCategoryLogger(LogCategoryConstant.APS_MIDWARE, clazz);
    }

    /**
     * 获取性能日志Logger
     */
    public static Logger getProfileLogger(Class<?> clazz) {
        return getCategoryLogger(LogCategoryConstant.APS_PROFILE, clazz);
    }

    /**
     * 获取链路追踪日志Logger
     */
    public static Logger getTraceLogger(Class<?> clazz) {
        return getCategoryLogger(LogCategoryConstant.APS_TRACE, clazz);
    }

    /**
     * 获取批处理日志Logger
     */
    public static Logger getBatchLogger(Class<?> clazz) {
        return getCategoryLogger(LogCategoryConstant.APS_BATCH, clazz);
    }

    /**
     * 获取告警日志Logger
     */
    public static Logger getAlertLogger() {
        return getLogger(LogCategoryConstant.APS_ALERT);
    }

    /**
     * 获取通讯日志Logger
     */
    public static Logger getLinkLogger() {
        return getLogger(LogCategoryConstant.APS_LINKS);
    }

    /**
     * 获取慢SQL日志Logger
     */
    public static Logger getShowSqlLogger() {
        return getLogger(LogCategoryConstant.APS_SLOWSQL);
    }

    /**
     * 获取SQL日志Logger
     */
    public static Logger getSqlLogger() {
        return getLogger(LogCategoryConstant.APS_SQL);
    }

    /**
     * 根据日志名称获取Logger实例
     *
     * @param loggerName 日志名称
     * @return Logger实例
     */
    public static Logger getLogger(String loggerName) {
        return createLogger(loggerName);
    }

    /**
     * 根据类获取Logger实例
     *
     * @param clazz 类
     * @return Logger实例
     */
    public static Logger getLogger(Class<?> clazz) {
        return createLogger(clazz);
    }

    /**
     * 创建Logger实例
     */
    private static Logger createLogger(Object param) {
        if (isLog4j2Available()) {
            return param instanceof Class ? new Log4j2Logger((Class<?>) param) : new Log4j2Logger(param.toString());
        }
        if (isLogbackAvailable()) {
            return param instanceof Class ? new LogbackLogger((Class<?>) param) : new LogbackLogger(param.toString());
        }
        throw new UnsupportedOperationException("未找到可用的日志实现框架(Log4j2或Logback)!");
    }

    /**
     * 检查Log4j2是否可用
     */
    private static boolean isLog4j2Available() {
        try {
            Class.forName("org.apache.logging.log4j.core.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 检查Logback是否可用
     */
    private static boolean isLogbackAvailable() {
        try {
            Class.forName("ch.qos.logback.classic.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
