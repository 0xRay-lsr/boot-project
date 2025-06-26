package cn.aps.boot.logs.listener;

import cn.aps.boot.logs.api.constants.LogCategoryConstant;
import cn.aps.boot.logs.core.LoggerFactory;
import cn.aps.boot.logs.core.utils.LoggerUtils;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.lang.NonNull;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description : 日志配置
 * @Author : lishirui
 * @Date ：2025/4/2 15:02
 */
public class LogSystemConfiguration implements ApplicationListener<ApplicationEvent>, Ordered {

    private final DeferredLog BOOT_LOGGER;

    private static final String OS_NAME = "os.name";
    private static final String LOG_MODE = "log.mode";
    private static final String WINDOW = "Window";
    private static final String CONSOLE = "console";
    private static final String FILE = "file";
    public static final AtomicBoolean BOOT_LOGGERS = new AtomicBoolean(false);

    public LogSystemConfiguration() {
        BOOT_LOGGER = new DeferredLog();
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
        if (BOOT_LOGGERS.compareAndSet(false, true)) {
            if (event instanceof ApplicationStartingEvent) {
                RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
                String os = System.getProperty(OS_NAME);
                BOOT_LOGGER.info("======= APS-LOG initialize, OS: " + os + " =======");
                //读取资源文件
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                if (LoggerUtils.isLog4j2Available()) {
                    System.setProperty("log4j.configurationFile", "classpath:aps-log4j2.xml");
                } else if (LoggerUtils.isLogbackAvailable()) {
                    System.setProperty("logging.config", "classpath:logback-spring.xml");
                } else {
                    throw new RuntimeException("No log4j2 or logback configuration file is available");
                }
                if (os.contains(WINDOW) && System.getProperty(LOG_MODE) == null) {
                    System.setProperty(LOG_MODE, CONSOLE);
                }

                String logMode = StrUtil.nullToDefault(System.getProperty(LOG_MODE), FILE);
                switch (logMode) {
                    case CONSOLE:
                        BOOT_LOGGER.info("======= The log mode is printed to [console] =======");
                        break;
                    case FILE:
                        BOOT_LOGGER.info("======= The log mode is printed to [file] =======");
                        break;
                    default:
                        BOOT_LOGGER.warn("======= The log mode is [N/A] =======");
                }
            } else if (event instanceof ApplicationPreparedEvent) {
                BOOT_LOGGER.switchTo(LoggerFactory.getLogger(LogCategoryConstant.APS_BOOT).getClass());
            }
        }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 22;
    }
}
