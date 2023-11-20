package com.feng.fengchat.common.common.event.listener;

import com.feng.fengchat.common.common.domain.enums.IdempotentEnum;
import com.feng.fengchat.common.common.domain.enums.ItemEnum;
import com.feng.fengchat.common.common.event.UserRegisteEvent;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.service.IUserBackpackService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 * @author jiangfeng
 * @date 2023/11/20
 */
@Component
public class UserRegisteListener {

    @Resource
    private IUserBackpackService userBackpackService;
    @EventListener(value = UserRegisteEvent.class)
    @Async
    public void sengCode(UserRegisteEvent event){
        User user = event.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID, user.getId().toString());
    }
}
