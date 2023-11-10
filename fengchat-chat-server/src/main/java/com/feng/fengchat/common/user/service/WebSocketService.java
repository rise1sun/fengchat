package com.feng.fengchat.common.user.service;

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
     * @param channelHandlerContext
     * @throws WxErrorException
     */
    void handleLoginReq(ChannelHandlerContext channelHandlerContext) throws WxErrorException;
}
