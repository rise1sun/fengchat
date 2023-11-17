package com.feng.fengchat.common.user.service;

import com.feng.fengchat.common.common.domain.dto.RequestInfo;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.domain.vo.resp.BadgeInfoResp;
import com.feng.fengchat.common.user.domain.vo.resp.UserInfoResp;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author jiangfeng
 * @since 2023-11-10
 */
public interface UserService {

    /**
     * 用户注册
     * @param insert
     * @return
     */
    Long registered(User insert);

    /**
     * 用户登录
     * @param id
     * @return
     */
    String login(Long id);

    /**
     * 获取用户信息
     * @param uid
     * @return
     */
    UserInfoResp getUserInfo(Long uid);

    /**
     * 修改用户名
     * @param uid
     * @param name
     */
    void modifyName(Long uid,String name);

    List<BadgeInfoResp> getBadgeList(Long uid);

    void wearingBadge(Long itemId, Long uid);
}
