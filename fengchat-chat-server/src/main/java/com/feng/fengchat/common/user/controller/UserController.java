package com.feng.fengchat.common.user.controller;


import com.feng.fengchat.common.common.domain.dto.RequestInfo;
import com.feng.fengchat.common.common.domain.vo.response.ApiResult;
import com.feng.fengchat.common.common.utils.RequestHolder;
import com.feng.fengchat.common.user.dao.UserDao;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.domain.vo.req.BadgeReq;
import com.feng.fengchat.common.user.domain.vo.req.ModifyNameReq;
import com.feng.fengchat.common.user.domain.vo.resp.BadgeInfoResp;
import com.feng.fengchat.common.user.domain.vo.resp.UserInfoResp;
import com.feng.fengchat.common.user.service.LoginService;
import com.feng.fengchat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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

    @Resource
    private UserService userService;

    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo() {
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    @PutMapping("/modifyName")
    @ApiOperation(value = "改名", notes = "改名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq req) {
        userService.modifyName(RequestHolder.get().getUid(),req.getName());
        return ApiResult.success();
    }


    @GetMapping("/getBadgeList")
    @ApiOperation(value = "徽章列表", notes = "徽章列表")
    public ApiResult<List<BadgeInfoResp>> getBadgeList() {
        return ApiResult.success(userService.getBadgeList(RequestHolder.get().getUid()));
    }
}

