package com.feng.fengchat.common.user.service.impl;

import com.feng.fengchat.common.common.constant.RedisKey;
import com.feng.fengchat.common.common.utils.JwtUtils;
import com.feng.fengchat.common.common.utils.RedisUtils;
import com.feng.fengchat.common.user.dao.UserDao;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author jiangfeng
 * @date 2023/11/13
 */
@Service
public class UserServiceImpl implements UserService {

    private static final long TOKEN_EXPIRE_DAYS = 3;
    @Resource
    private UserDao userDao;

    @Resource
    private JwtUtils jwtUtils;

    @Override
    @Transactional
    public Long registered(User insert) {
        userDao.save(insert);
        //todo 注册监听

        return insert.getId();
    }

    @Override
    public String login(Long id) {
        String token = jwtUtils.createToken(id);
        RedisUtils.set(getUserTokenKey(id), token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    private String getUserTokenKey(Long id) {
        String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, id);
        return key;
    }
}
