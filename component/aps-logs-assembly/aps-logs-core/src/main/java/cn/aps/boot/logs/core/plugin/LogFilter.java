package cn.aps.boot.logs.core.plugin;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;


/**
 * @Description : log拦截器，扩展预留
 * @Author : lishirui
 * @Date ：2025/3/27 19:22
 */
@Plugin(name = "LogFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
public class LogFilter extends AbstractFilter {

    public LogFilter(Result onMatch, Result onMismatch) {
        super(onMatch, onMismatch);
    }

    @PluginFactory
    public static LogFilter createFilter(@PluginAttribute("onMatch") final Result match,
                                         @PluginAttribute("onMisMatch") final Result mismatch) {
        return new LogFilter(match, mismatch);
    }

    public Result filter(Level currentLogLevel, LogEvent logEvent) {
        Level confLevel = getConfLevel(logEvent);
        if (confLevel == null) {
            return Result.NEUTRAL;
        }
        if (confLevel.intLevel() >= currentLogLevel.intLevel()) {
            return Result.ACCEPT;
        }
        return Result.DENY;
    }

    private Level getConfLevel(LogEvent event) {
        // TODO 动态日志获取
        return null;
    }

    @Override
    public Result filter(LogEvent event) {
        Level confLevel = getConfLevel(event);
        if (confLevel == null) {
            return Result.NEUTRAL;
        }
        if (confLevel.intLevel() >= event.getLevel().intLevel()) {
            return Result.ACCEPT;
        }
        return Result.DENY;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0,
                         Object p1) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1,
                         Object p2) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1,
                         Object p2, Object p3) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1,
                         Object p2, Object p3, Object p4) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1,
                         Object p2, Object p3, Object p4, Object p5) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1,
                         Object p2, Object p3, Object p4, Object p5, Object p6) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1,
                         Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1,
                         Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        return this.filter(level, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1,
                         Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return this.filter(level, null);
    }

}
