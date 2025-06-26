package cn.aps.boot.jdk17.demo.boot;

import org.springframework.util.StopWatch;

/**
 * @Description :系统状态上下文
 * @Author : lishirui
 * @Date ：2025/6/17 20:06
 */
public class SystemStateGlobalContext {

    private static SystemStateGlobalContext instance = new SystemStateGlobalContext();

    private SystemState state;

    private StopWatch stopwatch = new StopWatch();

    public StopWatch getStopwatch() {
        return stopwatch;
    }

    public void setStopwatch(StopWatch stopwatch) {
        this.stopwatch = stopwatch;
    }

    public static SystemStateGlobalContext get() {
        return instance;
    }

    public SystemState getState() {
        return state;
    }

    public void setState(SystemState state) {
        this.state = state;
    }

    public static enum SystemState {
        STARTING("SystemStatus:Starting"),
        STARTED("SystemStatus:Started"),
        STOPPING("SystemStatus:Stopping"),
        STOPPED("SystemStatus:Stopped"),
        FAILED("SystemStatus:Failed");
        private final String value;

        SystemState(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
