package com.feng.fengchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author jiangfeng
 * @date 2023/11/16
 */
@Data
public class ModifyNameReq {

    @NotBlank(message = "用户名不能为空")
    @Length(max = 6, message = "用户名可别取太长，不然我记不住噢")
    @ApiModelProperty(value = "用户名")
    private String name;
}
