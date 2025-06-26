package cn.aps.boot.logs.core;

import cn.aps.boot.logs.api.config.AggregatorConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.Marker;

/**
 * @Description : 基于log4j2 实现
 * @Author : lishirui
 * @Date ：2025/3/27 16:45
 */
public class Log4j2Logger extends AbstractLogger implements cn.aps.boot.logs.api.Logger {

    private final Logger logger;

    public Log4j2Logger(Class<?> clazz) {
        super(clazz.getName());
        this.logger = LogManager.getLogger(clazz);
        //TODO 考虑java springboot 实现
        setAggregator(new AggregatorConfig());
    }

    public Log4j2Logger(String name) {
        super(name);
        this.logger = LogManager.getLogger(name);
        //TODO 考虑java springboot 实现
        setAggregator(new AggregatorConfig());
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        if (isDebugEnabled()) {
            logger.debug(msg);
            sendToAggregator("DEBUG", msg, null);
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (isDebugEnabled()) {
            logger.debug(format, arg);
            sendToAggregator("DEBUG", String.format(format, arg), null);
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            logger.debug(format, arguments);
            sendToAggregator("DEBUG", String.format(format, arguments), null);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (isDebugEnabled()) {
            logger.debug(msg, t);
            sendToAggregator("DEBUG", msg, t);
        }
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(Marker marker, String msg) {
        logger.debug(msg, marker);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        logger.debug(format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        logger.debug(format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        logger.debug(format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        logger.debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        if (isInfoEnabled()) {
            logger.info(msg);
            sendToAggregator("INFO", msg, null);
        }
    }

    @Override
    public void info(String format, Object arg) {
        if (isInfoEnabled()) {
            logger.info(format, arg);
            sendToAggregator("INFO", String.format(format, arg), null);
        }
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        if (isInfoEnabled()) {
            logger.info(format, arguments);
            sendToAggregator("INFO", String.format(format, arguments), null);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if (isInfoEnabled()) {
            logger.info(msg, t);
            sendToAggregator("INFO", msg, t);
        }
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(Marker marker, String msg) {
        logger.info(msg, marker);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        logger.info(format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        logger.info(format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        logger.info(format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        logger.info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        if (isWarnEnabled()) {
            logger.warn(msg);
            sendToAggregator("WARN", msg, null);
        }
    }

    @Override
    public void warn(String format, Object arg) {
        if (isWarnEnabled()) {
            logger.warn(format, arg);
            sendToAggregator("WARN", String.format(format, arg), null);
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (isWarnEnabled()) {
            logger.warn(format, arguments);
            sendToAggregator("WARN", String.format(format, arguments), null);
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (isWarnEnabled()) {
            logger.warn(msg, t);
            sendToAggregator("WARN", msg, t);
        }
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(Marker marker, String msg) {
        logger.warn(msg, marker);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        logger.warn(format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        logger.warn(format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        logger.warn(format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        logger.warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        if (isErrorEnabled()) {
            logger.error(msg);
            sendToAggregator("ERROR", msg, null);
        }
    }

    @Override
    public void error(String format, Object arg) {
        if (isErrorEnabled()) {
            logger.error(format, arg);
            sendToAggregator("ERROR", String.format(format, arg), null);
        }
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        logger.error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        if (isErrorEnabled()) {
            logger.error(format, arguments);
            sendToAggregator("ERROR", String.format(format, arguments), null);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        if (isErrorEnabled()) {
            logger.error(msg, t);
            sendToAggregator("ERROR", msg, t);
        }
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(Marker marker, String msg) {
        logger.error(msg, marker);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        logger.error(format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        logger.error(format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        logger.error(format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        logger.error(msg, t);
    }
}
