package cn.aps.boot.test.aop;

import cn.aps.boot.test.annotation.Timeout;
import cn.aps.boot.test.exception.BusinessTimeoutException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @Timeout注解的AOP切面实现
 * 拦截被@Timeout注解的方法，并使用独立的线程池执行，以实现超时监控。
 * (此实现已被新的Watchdog模式替代，暂时禁用)
 *
 * @Author lishirui
 * @Date 2025-08-21
 */
//@Aspect
//@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TimeoutAspect {

    // 使用缓存的线程池来处理瞬时高并发的业务请求
    // 避免使用固定大小的线程池，因为如果所有线程都被长时间任务占用，会产生拒绝服务的问题
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Around("@annotation(cn.aps.boot.test.annotation.Timeout)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Timeout timeout = method.getAnnotation(Timeout.class);

        Callable<Object> callable = () -> {
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                if (throwable instanceof Exception) {
                    throw (Exception) throwable;
                } else {
                    throw new Exception(throwable);
                }
            }
        };

        Future<Object> future = executorService.submit(callable);

        try {
            return future.get(timeout.value(), timeout.unit());
        } catch (TimeoutException e) {
            // 关键：超时后，向执行任务的线程发送中断信号
            future.cancel(true);
            String methodName = signature.getMethod().getName();
            throw new BusinessTimeoutException("方法 [" + methodName + "] 执行超时 (限制: " + timeout.value() + " " + timeout.unit().toString().toLowerCase() + ")");
        } catch (Exception e) {
            // 如果执行过程中发生其他异常，也需要取消任务并重新抛出
            future.cancel(true);
            throw e;
        }
    }
}
