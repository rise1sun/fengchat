package com.feng.fengchat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author jiangfeng
 * @date 2023/11/15
 */
@Data
@ApiModel
public class UserInfoResp {

    @ApiModelProperty("用户id")
    private Long uid;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("昵称")
    private String avatar;
    @ApiModelProperty("个性签名")
    private String sign;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("性别")
    private Integer sex;
    @ApiModelProperty("剩余改名卡次数")
    private Integer chance;
}
