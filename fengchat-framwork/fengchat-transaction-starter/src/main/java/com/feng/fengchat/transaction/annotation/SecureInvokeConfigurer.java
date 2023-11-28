package com.feng.fengchat.transaction.annotation;

import java.util.concurrent.Executor;

/**
 *
 * @author jiangfeng
 * @date 2023/11/28
 */
public interface SecureInvokeConfigurer {

    /**
     * 获取异步执行器，给子类实现
     * @return
     */
    default Executor getSecureInvokeExecutor(){
        return null;
    }
}
