package com.feng.fengchat.common.common.exception;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author jiangfeng
 * @date 2023/11/20
 */
@Slf4j
public class GlobalUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

    public static final GlobalUncaughtExceptionHandler instance = new GlobalUncaughtExceptionHandler();
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread {} ", t.getName(), e);
    }

    private GlobalUncaughtExceptionHandler() {
    }

    public static  GlobalUncaughtExceptionHandler getInstance(){
        return instance;
    }
}
