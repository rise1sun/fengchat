package com.feng.fengchat.transaction.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author jiangfeng
 * @date 2023/11/28
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecureInvoke {

    /**
     * 默认最大重试次数
     * @return
     */
    int MaxTryTime() default 3;

    /**
     * 默认异步
     * @return
     */
    boolean async() default  true;
}
