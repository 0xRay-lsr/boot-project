package cn.aps.boot.test.handler;

import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;

/**
 * WebFlux全局异常处理器，用于捕获WebFilter阶段的异常。
 * @author lishirui
 * @date 2025-08-21
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 确保此异常处理器最先被调用
public class TimeoutWebExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        System.out.println("--- [WebExceptionHandler] 捕获到异常: " + ex.getClass().getName() + " ---");

        if (ex instanceof TimeoutException) {
            System.out.println("--- [WebExceptionHandler] 识别为 TimeoutException，返回 408 ---");
            exchange.getResponse().setStatusCode(HttpStatus.REQUEST_TIMEOUT);
            exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap("Request Timeout".getBytes())));
        }
        // 如果是其他异常，则让Spring的默认异常处理器处理
        return Mono.error(ex);
    }
}
