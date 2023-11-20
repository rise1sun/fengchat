package com.feng.fengchat.common.websocket;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlQuery;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.apache.http.client.utils.URIBuilder;


;import java.util.Optional;

/**
 *
 * @author jiangfeng
 * @date 2023/11/14
 */
public class MyHandShakerAuthHandler extends ChannelInboundHandlerAdapter {

    public  void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpRequest httpRequest = (HttpRequest)msg;
            String uri = httpRequest.uri();
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(uri);
            String token = Optional.ofNullable(urlBuilder)
                    .map(UrlBuilder::getQuery)
                    .map(k -> k.get("token"))
                    .map(CharSequence::toString).orElse("");
            NettyUtils.setAttr(ctx.channel(), NettyUtils.TOKEN, token);
            httpRequest.setUri(urlBuilder.getPath().toString());
        }
        ctx.fireChannelRead(msg);

    }
}
