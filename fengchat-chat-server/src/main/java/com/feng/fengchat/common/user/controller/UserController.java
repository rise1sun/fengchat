package com.feng.fengchat.common.user.controller;


import com.feng.fengchat.common.common.domain.vo.response.ApiResult;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.domain.vo.resp.UserInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author jiangfeng
 * @since 2023-11-10
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/capi/user")
public class UserController {


    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo() {

        return null;
    }

}

