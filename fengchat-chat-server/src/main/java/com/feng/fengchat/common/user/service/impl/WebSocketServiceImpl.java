package com.feng.fengchat.common.user.service.impl;

import com.feng.fengchat.common.user.service.WebSocketService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author jiangfeng
 * @date 2023/11/10
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Resource
    private WxMpService wxMpService;

    @Override
    public void handleLoginReq(ChannelHandlerContext channelHandlerContext) throws WxErrorException {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 1800);
        String url = wxMpQrCodeTicket.getUrl();

        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(url));

    }
}
