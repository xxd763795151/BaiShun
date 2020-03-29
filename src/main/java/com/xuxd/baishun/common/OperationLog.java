package com.xuxd.baishun.common;

import com.xuxd.baishun.beans.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: 许晓东
 * @Date: 20-3-29 22:02
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OperationLog {

    String id();
    String content() default "";
    OperationType type() default OperationType.create;
}
