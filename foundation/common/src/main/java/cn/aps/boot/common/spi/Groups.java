package cn.aps.boot.common.spi;

import java.lang.annotation.*;

/**
 * @Description : 定义spi 分组实现
 * @Author : lishirui
 * @Date ：2025/3/27 19:22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Groups {

    /**
     * Group list
     *
     * @return
     */
    String[] value() default {};
}
