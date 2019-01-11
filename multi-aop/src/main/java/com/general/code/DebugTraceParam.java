package com.general.code;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: zml
 * Date  : 2019/1/11 - 13:38
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DebugTraceParam {
    String value() default "";
    String param() default "";
    Class clz() default Object.class;
}
