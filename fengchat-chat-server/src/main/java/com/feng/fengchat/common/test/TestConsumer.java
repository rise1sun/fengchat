package com.feng.fengchat.common.test;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author jiangfeng
 * @date 2023/11/28
 */

@RocketMQMessageListener(consumerGroup = "test-group", topic = "test-topic")
@Component
public class TestConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String dto) {
        System.out.println("收到消息{}"+dto);
    }
}


