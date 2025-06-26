package cn.aps.boot.logs.core.route;

import cn.aps.boot.logs.api.constants.LogRouteTypeEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description : 日志路由管理器
 * @Author : lishirui
 * @Date ：2025/4/8 17:00
 */
public class LogRouteManager {

    private static final Map<String, LogRouteTypeEnum> routeMap = new ConcurrentHashMap<>();

    /**
     * 设置路由类型
     * private static final Logger logger = LoggerFactory.getLogger(LogRouteExample.class);
     * LogRouteManager.setRoute(LogRouteExample.class.getName(), LogRouteTypeEnum.APP);
     * logger.info("This is an application log");
     *
     * @param loggerName
     * @param routeType
     */
    public static void setRoute(String loggerName, LogRouteTypeEnum routeType) {
        routeMap.put(loggerName, routeType);
    }

    public static LogRouteTypeEnum getRoute(String loggerName) {
        return routeMap.getOrDefault(loggerName, LogRouteTypeEnum.app);
    }

    public static void clearRoute(String loggerName) {
        routeMap.remove(loggerName);
    }
}
