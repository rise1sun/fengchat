package com.feng.fengchat.common.common.event;

import com.feng.fengchat.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author jiangfeng
 * @date 2023/11/20
 */
@Getter
public class UserRegisteEvent extends ApplicationEvent {

    private User user;
    public UserRegisteEvent(Object source, User user) {
        super(source);
        this.user =user;
    }
}
