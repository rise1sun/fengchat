package com.feng.fengchat.common.websocket;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 *
 * @author jiangfeng
 * @date 2023/11/15
 */
public class NettyUtils {

    public static final AttributeKey<String> TOKEN  = AttributeKey.valueOf("token");
    public static final AttributeKey<String> IP  = AttributeKey.valueOf("ip");

    public static <T> void setAttr(Channel channel, AttributeKey<T> key, T value) {
        Attribute<T> attr = channel.attr(key);
        attr.set(value);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> key) {
        Attribute<T> attr = channel.attr(key);
        return attr.get();
    }

}
