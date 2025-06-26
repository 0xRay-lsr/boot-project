package cn.aps.boot.jdk17.demo.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StopWatch;

import java.time.Duration;

/**
 * @Description : 统一扩展启动生命周期
 * @Author : lishirui
 * @Date ：2025/6/17 16:19
 */
public class ApplicationGlobalListener implements SpringApplicationRunListener, Ordered {
    public static final Logger logger = LoggerFactory.getLogger(ApplicationGlobalListener.class);
    public static DeferredLog BOOT_LOGGER = new DeferredLog();
    private final SpringApplication application;
    private final SimpleApplicationEventMulticaster springApplicationEventMulticaster;


    public ApplicationGlobalListener(SpringApplication application, SimpleApplicationEventMulticaster initialMulticaster) {
        this.application = application;
        this.springApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
        for (ApplicationListener<?> listener : application.getListeners()) {
            this.springApplicationEventMulticaster.addApplicationListener(listener);
        }
        if (this.application.getWebApplicationType() == WebApplicationType.NONE) {
            return;
        }
        //TODO 输出 springboot版本 jdk ，gc 等 参数
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        // 如果是springcloud环境不执行
        if (this.application.getWebApplicationType() == WebApplicationType.NONE) {
            return;
        }
        StopWatch stopWatch = SystemStateGlobalContext.get().getStopwatch();
        stopWatch.start("ApplicationGlobalListener starting");
        SystemStateGlobalContext.get().setState(SystemStateGlobalContext.SystemState.STARTING);
        stopWatch.stop();
        logger.info("ApplicationGlobalListener starting total time: {}", stopWatch.getTotalTimeMillis());
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {

        SpringApplicationRunListener.super.environmentPrepared(bootstrapContext, environment);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        SpringApplicationRunListener.super.contextPrepared(context);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        SpringApplicationRunListener.super.contextLoaded(context);
    }

    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        SpringApplicationRunListener.super.started(context, timeTaken);
    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        SpringApplicationRunListener.super.ready(context, timeTaken);
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
