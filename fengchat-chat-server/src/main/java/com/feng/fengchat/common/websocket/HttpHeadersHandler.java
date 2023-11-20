package com.feng.fengchat.common.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Objects;

/**
 *
 * @author jiangfeng
 * @date 2023/11/20
 */
public class HttpHeadersHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest){
            HttpHeaders headers = ((FullHttpRequest) msg).headers();
            String ip = headers.get("X-Real-IP");
            if (Objects.isNull(ip)) {
                //没过nginx直接获取远端地址
                InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
                ip = address.getAddress().getHostAddress();
            }
            NettyUtils.setAttr(ctx.channel(),NettyUtils.IP,ip);
            //用一次就不需要了
            ctx.pipeline().remove(this);
            ctx.fireChannelRead(msg);

        }else{
            ctx.fireChannelRead(msg);
        }
           }

}
