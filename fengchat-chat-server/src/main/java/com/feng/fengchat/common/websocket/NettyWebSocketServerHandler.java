package com.feng.fengchat.common.websocket;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.feng.fengchat.common.user.domain.vo.enums.WSReqTypeEnum;
import com.feng.fengchat.common.user.domain.vo.enums.WSRespTypeEnum;
import com.feng.fengchat.common.user.domain.vo.req.WSBaseReq;
import com.feng.fengchat.common.user.service.WebSocketService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

import javax.annotation.Resource;

/**
 *
 * @author jiangfeng
 * @date 2023/11/10
 */
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    private WebSocketService webSocketService;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOffLine(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        WebSocketService service = this.getService();
        this.webSocketService =service;
        webSocketService.connect(ctx.channel());
    }

    private WebSocketService getService() {
        WebSocketService bean = SpringUtil.getBean(WebSocketService.class);
        return bean;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            System.out.println("握手完成");
        }else if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            userOffLine(ctx);
        }
    }

    private void userOffLine(ChannelHandlerContext ctx) {
        webSocketService.offLine(ctx.channel());
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        WSBaseReq req = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(req.getType())) {
            case LOGIN:
                this.webSocketService.handleLoginReq(channelHandlerContext.channel());
                break;
            case AUTHORIZE:
                webSocketService.handleAuthSuccess(channelHandlerContext.channel(),req.getDate());
                break;
            case HEARTBEAT:
                break;
        }
    }
}
