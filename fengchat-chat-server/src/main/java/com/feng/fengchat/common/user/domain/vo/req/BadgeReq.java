package com.feng.fengchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

/**
 *
 * @author jiangfeng
 * @date 2023/11/17
 */
@Data
@ApiModel("徽章请求")
public class BadgeReq {

    @ApiModelProperty(value = "徽章id")
    @NonNull
    private Long itemId;
}
