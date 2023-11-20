package com.feng.fengchat.common.common.event.listener;

import com.feng.fengchat.common.common.event.UserOnlineEvent;
import com.feng.fengchat.common.user.dao.UserDao;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.service.IpService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;

/**
 *
 * @author jiangfeng
 * @date 2023/11/20
 */
public class UserOnlineListener {

    @Resource
    private UserDao userDao;

    @Resource
    private IpService ipService;
    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveDB(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        //update.setActiveStatus(ChatActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(update);
        //更新用户ip详情
        ipService.refreshIpDetailAsync(user.getId());
    }
}
