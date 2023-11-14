package com.feng.fengchat.common.user.service;

import com.feng.fengchat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author jiangfeng
 * @since 2023-11-10
 */
public interface UserService {

    Long registered(User insert);

    String login(Long id);
}
