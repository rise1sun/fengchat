package com.feng.fengchat.common.user.dao;

import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jiangfeng
 * @since 2023-11-10
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User>{

    public User getUserByOpenId(String openId) {
        return lambdaQuery().eq(User::getOpenId,openId)
                        .one();
    }
}
