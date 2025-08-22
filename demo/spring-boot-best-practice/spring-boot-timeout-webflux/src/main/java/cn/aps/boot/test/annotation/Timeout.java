package cn.aps.boot.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 自定义方法超时注解
 * 用于标记在指定时间内必须完成的方法，否则将被中断。
 *
 * @Author lishirui
 * @Date 2025-08-21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Timeout {

    /**
     * 超时时间数值
     */
    long value();

    /**
     * 超时时间单位，默认为毫秒
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
