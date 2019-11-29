package com.groot.flow.spi;

import java.lang.annotation.*;

/**
 * @author chenhaitao
 * @date 2019-11-25
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {
    String key() default "";
    String value() default "";
}
