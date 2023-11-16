package com.feng.fengchat.common.common.utils;

import com.feng.fengchat.common.common.domain.dto.RequestInfo;

/**
 *
 * @author jiangfeng
 * @date 2023/11/16
 */
public class  RequestHolder {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();
    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
