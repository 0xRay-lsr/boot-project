package cn.aps.boot.logs.api.config;

/**
 * @Description : 日志聚合配置类
 * @Author : lishirui
 * @Date ：2025/4/14 15:41
 */
public class AggregatorConfig {
    private String spiId = "default";
    private ELKConfig elkConfig;
    /**
     * 线程池配置
     */
    private int queueSize = 10000;
    private int corePoolSize = 2;
    private int maxPoolSize = 4;
    private long keepAliveTime = 60L;

    public String getSpiId() {
        return spiId;
    }

    public void setSpiId(String spiId) {
        this.spiId = spiId;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public ELKConfig getElkConfig() {
        return elkConfig;
    }

    public void setElkConfig(ELKConfig elkConfig) {
        this.elkConfig = elkConfig;
    }
}
