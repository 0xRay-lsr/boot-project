package cn.aps.boot.logs.api.aggregator;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description : 日志事件，承载日志msg
 * @Author : lishirui
 * @Date ：2025/4/14 15:41
 */
public class LogEvent {
    private String loggerName;
    private String level;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> context;
    private Throwable throwable;

    public LogEvent(String loggerName, String level, String message) {
        this.loggerName = loggerName;
        this.level = level;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
} 