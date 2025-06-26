package cn.aps.boot.common.spi;

import java.lang.annotation.*;

/**
 * @Description : 定义spi 扩展实现元数据注释
 * @Author : lishirui
 * @Date ：2025/3/27 19:22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPIMeta {

    /**
     * Extension Implementation ID
     *
     * @return
     */
    String id() default "";
}
