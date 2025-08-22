package cn.aps.boot.socket.demo.pool.exception;

/**
 * @Description 当连接池无法提供可用连接时抛出的异常
 * @Author lishirui
 * @Date 2025/8/18
 */
public class NoConnectionAvailableException extends Exception {

    public NoConnectionAvailableException(String message) {
        super(message);
    }

    public NoConnectionAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
