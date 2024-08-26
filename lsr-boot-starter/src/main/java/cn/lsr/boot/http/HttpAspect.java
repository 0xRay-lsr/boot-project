package cn.lsr.boot.http;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2024/8/5 14:49
 */
@Aspect
@Component
public class HttpAspect {
    private static final Logger logger = LoggerFactory.getLogger("http-log");

    @Pointcut("execution(* cn.lsr.*.controller..*.*(..))")
    public void pointCut() {

    }

    @Around(value = "pointCut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //获取本次接口的唯一码
        String token = java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        MDC.put("traceId", token);

        //result的值就是被拦截方法的返回值
        try {
            // 获取目标参数
            String serviceUniqueName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
            String methodName = proceedingJoinPoint.getSignature().getName();
            long start = System.currentTimeMillis();
            Object proceed = proceedingJoinPoint.proceed();
            logger.info("@Http:{}.{},耗时:{}ms", serviceUniqueName, methodName,
                    System.currentTimeMillis() - start);
            return proceed;
        } catch (Exception e) {
            logger.error("@Http请求出错", e);
            throw e;
        } finally {
            MDC.remove("traceId");
        }

    }

}
