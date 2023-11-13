package com.feng.fengchat.common.user.service.adapter;

import cn.hutool.core.util.RandomUtil;
import com.feng.fengchat.common.user.domain.entity.User;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
 *
 * @author jiangfeng
 * @date 2023/11/13
 */
public class UserAdapter {

    public static User buildUser(String openId) {
        return User.builder().openId(openId).build();
    }

    public static User buildAuthUser(WxOAuth2UserInfo userInfo, Long id) {
        User user = new User();
        user.setId(id);
        user.setAvatar(userInfo.getHeadImgUrl());
        user.setName(userInfo.getNickname());
        user.setSex(userInfo.getSex());
        if (userInfo.getNickname().length() > 6) {
            user.setName("名字过长" + RandomUtil.randomInt(100000));
        } else {
            user.setName(userInfo.getNickname());
        }
        return user;
    }
}
