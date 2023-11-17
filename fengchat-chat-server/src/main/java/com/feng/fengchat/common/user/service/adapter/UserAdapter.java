package com.feng.fengchat.common.user.service.adapter;

import cn.hutool.core.util.RandomUtil;
import com.feng.fengchat.common.common.domain.enums.YesOrNoEnum;
import com.feng.fengchat.common.user.domain.entity.ItemConfig;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.domain.entity.UserBackpack;
import com.feng.fengchat.common.user.domain.vo.resp.BadgeInfoResp;
import com.feng.fengchat.common.user.domain.vo.resp.UserInfoResp;
import jodd.bean.BeanCopy;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.beans.BeanUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static UserInfoResp buildUserInfoResp(User user,Integer countModfiyNameCard) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtils.copyProperties(user, userInfoResp);
        userInfoResp.setModifyNameChance(countModfiyNameCard);
        return userInfoResp;
    }

    public static List<BadgeInfoResp> buildBadgeResp(List<ItemConfig> itemConfigs, List<UserBackpack> backpacks, User user) {
        Set<Long> collect = backpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
        return itemConfigs.stream().map(m -> {
                    BadgeInfoResp resp = new BadgeInfoResp();
                    BeanUtils.copyProperties(m, resp);
                    resp.setObtain(collect.contains(m.getId())? YesOrNoEnum.YES.getStatus() :YesOrNoEnum.NO.getStatus());
                    resp.setWearing(Objects.equals(user.getItemId(),m.getId())?YesOrNoEnum.YES.getStatus() :YesOrNoEnum.NO.getStatus());
                    return resp;
                }).sorted(Comparator.comparing(BadgeInfoResp::getWearing).reversed().thenComparing(BadgeInfoResp::getObtain))
                        .collect(Collectors.toList());
    }
}
