package cn.aps.boot.test.handler;

import cn.aps.boot.test.exception.BusinessTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 全局异常处理器
 * 捕获特定的业务异常并转换为对客户端友好的HTTP响应。
 *
 * @Author lishirui
 * @Date 2025-08-21
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessTimeoutException.class)
    public ResponseEntity<Map<String, Object>> handleTimeoutException(BusinessTimeoutException ex) {
        Map<String, Object> body = Map.of(
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Service Unavailable",
                "message", ex.getMessage()
        );
        // 返回503服务不可用状态码
        return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
