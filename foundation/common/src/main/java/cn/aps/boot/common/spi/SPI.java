package cn.aps.boot.common.spi;

import java.lang.annotation.*;

/**
 * @Description : 定义spi 注解
 * @Author : lishirui
 * @Date ：2025/3/27 19:22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface SPI {
	/**
	 * Default Implementation ID
	 *
	 * @return
	 */
	String defaultId() default "";
}
