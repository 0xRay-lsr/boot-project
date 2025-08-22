package cn.aps.boot.test.exception;

/**
 * 业务超时异常
 * 当一个被@Timeout注解标记的方法执行超时时，抛出此异常。
 *
 * @Author lishirui
 * @Date 2025-08-21
 */
public class BusinessTimeoutException extends RuntimeException {

    public BusinessTimeoutException(String message) {
        super(message);
    }

    public BusinessTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
