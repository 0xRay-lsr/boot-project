package cn.aps.boot.logs.api.utils;

import cn.aps.boot.logs.api.constants.LogRouteTypeEnum;
import org.slf4j.MDC;

import java.util.Iterator;
import java.util.Map;

/**
 * @Description : 日志mdc工具类
 * @Author : lishirui
 * @Date ：2025/3/31 15:52
 */
public class LogContextManager {

    private static final String DYNAMIC_LOG_TYPE = "log_type";

    /**
     * 动态设置日志类型
     *
     * @param logType
     */
    public static void setLogType(LogRouteTypeEnum logType) {
        MDC.put(DYNAMIC_LOG_TYPE, logType.toString());
    }

    /**
     * 获取当前日志类型
     *
     * @return
     */
    public static LogRouteTypeEnum getLogType() {
        String logTypeString = MDC.get(DYNAMIC_LOG_TYPE);
        return LogRouteTypeEnum.valueOf(logTypeString);
    }

    /**
     * 添加单个日志点
     *
     * @param key
     * @param value
     */
    public static void putLogMDC(String key, String value) {
        MDC.put(key, value);
    }

    /**
     * 添加日志点
     *
     * @param map key为日志key ，value为输出值
     */
    public static void putLogMDCMap(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            MDC.put(next.getKey(), next.getValue());
        }
    }

    /**
     * 清理MDC
     */
    public static void clear() {
        MDC.clear();
    }
}
