package com.feng.fengchat.common.user.service;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 *
 * @author jiangfeng
 * @date 2023/11/10
 */
public interface WebSocketService {

    /**
     * 获取二维码url
     * @param channel
     * @throws WxErrorException
     */
    void handleLoginReq(Channel channel) throws WxErrorException;

    void offLine(Channel channel);

    void connect(Channel channel);

    /**
     * 等待授权
     * @param code
     */
    void waitAuth(Integer code);

    void authSuccess(Integer integer, Long id);

    /**
     * 处理授权成功
     * @param token
     */
    void handleAuthSuccess(Channel channel,String token);
}
