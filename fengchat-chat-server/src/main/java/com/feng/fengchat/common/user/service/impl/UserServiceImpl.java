package com.feng.fengchat.common.user.service.impl;

import com.feng.fengchat.common.user.dao.UserDao;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 * @author jiangfeng
 * @date 2023/11/13
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    @Transactional
    public Long registered(User insert) {
        userDao.save(insert);
        //todo 注册监听

        return insert.getId();
    }
}
