package com.feng.fengchat.common.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author jiangfeng
 * @date 2023/11/17
 */
@AllArgsConstructor
@Getter
public enum IdempotentEnum {
    UID(1,"uid"),
    MSG_ID(2,"businessId"),;

    private Integer code;
    private String message;
}
