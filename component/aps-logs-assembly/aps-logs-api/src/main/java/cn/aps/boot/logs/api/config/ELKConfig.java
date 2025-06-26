package cn.aps.boot.logs.api.config;

/**
 * @Description : elk配置
 * @Author : lishirui
 * @Date ：2025/4/14 16:44
 */
public class ELKConfig {
    private String host;
    private int port;
    private String indexPrefix;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIndexPrefix() {
        return indexPrefix;
    }

    public void setIndexPrefix(String indexPrefix) {
        this.indexPrefix = indexPrefix;
    }
}
