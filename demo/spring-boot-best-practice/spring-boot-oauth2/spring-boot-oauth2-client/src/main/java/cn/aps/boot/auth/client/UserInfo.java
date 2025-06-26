package cn.aps.boot.auth.client;

/**
 * @Description : 用户信息DTO 用于返回给客户端的用户信息
 * @Author : lishirui
 * @Date ：2025/6/25 10:14
 */
public class UserInfo {
    private String username;
    private String scope; // 访问范围
    private String audience; // 受众

    public UserInfo(String username, String scope, String audience) {
        this.username = username;
        this.scope = scope;
        this.audience = audience;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", scope='" + scope + '\'' +
                ", audience='" + audience + '\'' +
                '}';
    }
}
