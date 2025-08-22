package cn.aps.boot.test.watchdog;

import cn.aps.boot.test.annotation.Timeout;
import cn.aps.boot.test.exception.BusinessTimeoutException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 基于看门狗模式的超时AOP切面
 * 将当前线程注册到看门狗服务，并在方法结束后取消注册。
 *
 * @Author lishirui
 * @Date 2025-08-21
 */
@Aspect
@Component
public class WatchdogTimeoutAspect {

    private final TimeoutWatchdog watchdog;

    public WatchdogTimeoutAspect(TimeoutWatchdog watchdog) {
        this.watchdog = watchdog;
    }

    @Around("@annotation(cn.aps.boot.test.annotation.Timeout)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Timeout timeout = signature.getMethod().getAnnotation(Timeout.class);
        long timeoutMillis = timeout.unit().toMillis(timeout.value());

        Thread currentThread = Thread.currentThread();
        watchdog.monitor(currentThread, timeoutMillis);

        try {
            return pjp.proceed();
        } catch (Throwable e) {
            // 捕获所有异常，并检查两个条件中的任何一个是否成立：
            // 1. 异常链的根因是InterruptedException (适用于Thread.sleep等可中断的阻塞方法)
            // 2. 当前线程的中断标志位是true (适用于在循环中检查Thread.currentThread().isInterrupted()的场景)
            if (isCausedByInterruptedException(e) || Thread.currentThread().isInterrupted()) {
                // 重新设置中断状态，这是一个好习惯，可以确保上游调用者也能感知到中断。
                Thread.currentThread().interrupt();
                String methodName = signature.getMethod().getName();
                throw new BusinessTimeoutException("方法 [" + methodName + "] 执行超时 (限制: " + timeout.value() + " " + timeout.unit().toString().toLowerCase() + ")", e);
            } else {
                // 如果不是中断导致的，原样抛出异常
                throw e;
            }
        } finally {
            // 确保线程在方法结束后被取消监控
            watchdog.unmonitor(currentThread);
        }
    }

    private boolean isCausedByInterruptedException(Throwable th) {
        if (th instanceof InterruptedException) {
            return true;
        }
        Throwable cause = th.getCause();
        while (cause != null) {
            if (cause instanceof InterruptedException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}
