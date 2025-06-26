package cn.aps.boot.logs;

import cn.aps.boot.logs.api.Logger;
import cn.aps.boot.logs.api.constants.LogRouteTypeEnum;
import cn.aps.boot.logs.api.utils.LogContextManager;
import cn.aps.boot.logs.core.LoggerFactory;
import org.slf4j.MDC;

public class LogMain {
    public static final Logger logger = LoggerFactory.getLogger(LogMain.class);
    public static final Logger bootLogger = LoggerFactory.getBootLogger(LogMain.class);
    public static final Logger appLogger = LoggerFactory.getAppLogger(LogMain.class);

    public static void main(String[] args) {
//        System.setProperty("log4j.configurationFile", "aps-log4j2.xml");
        System.setProperty("aps.systemId", "0001");
        System.setProperty("aps.applicationName", "logs-demo");
        System.setProperty("log.model", "file");
        // 强制重新加载 Log4j2 配置
//        Configurator.initialize(null, "aps-log4j2.xml");
        LogContextManager.setLogType(LogRouteTypeEnum.boot);

        logger.info("default logger ...");
        System.out.println("Before run: " + MDC.get("log_type"));

        System.out.println("After run: " + MDC.get("log_type"));
        bootLogger.info("boot logger ...");
        bootLogger.debug("boot logger debug ...");
        bootLogger.warn("boot logger warn ...");
        bootLogger.error("boot logger error ...");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LogContextManager.setLogType(LogRouteTypeEnum.app);
        appLogger.info("app logger ...");
        appLogger.debug("app logger debug ...");
        appLogger.warn("app logger warn ...");
        appLogger.error("app logger error ...");
        System.out.println("start demo success");
    }
}