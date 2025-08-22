package cn.aps.boot.test.filter;

import cn.aps.boot.test.annotation.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * WebFilter 实现的超时处理器
 * 在请求处理链中应用超时逻辑，替代AOP方案。
 * @author lishirui
 * @date 2025-08-21
 */
@Component
public class TimeoutWebFilter implements WebFilter {

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private HandlerMapping handlerMapping;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 1. 获取当前请求对应的HandlerMethod
        return handlerMapping.getHandler(exchange)
                .flatMap(handler -> {
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        // 2. 检查HandlerMethod上是否有@Timeout注解
                        Timeout timeoutAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Timeout.class);

                        if (timeoutAnnotation != null) {
                            // 3. 计算最终的超时时间
                            long timeoutValue = timeoutAnnotation.value();
                            TimeUnit timeoutUnit = timeoutAnnotation.unit();
                            long defaultTimeoutMillis = timeoutUnit.toMillis(timeoutValue);

                            ServerHttpRequest request = exchange.getRequest();
                            String timeoutHeader = request.getHeaders().getFirst("X-Request-Timeout");
                            long finalTimeoutMillis = timeoutHeader != null ? Long.parseLong(timeoutHeader) : defaultTimeoutMillis;

                            System.out.println("--- [WebFilter] 应用超时: " + finalTimeoutMillis + "ms 到方法: " + handlerMethod.getShortLogMessage() + " ---");

                            // 显式将 ServerHttpRequest 写入 Reactor Context 否则在超时处理中无法获取到请求头
                            return chain.filter(exchange)
                                    .contextWrite(context -> context.put(ServerHttpRequest.class, request))
                                    .timeout(Duration.ofMillis(finalTimeoutMillis));
                        }
                    }
                    // 如果没有@Timeout注解，或者不是HandlerMethod，则继续处理链
                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange)); // 如果handlerMapping.getHandler(exchange)返回空，则继续处理链
    }
}
