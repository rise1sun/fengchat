package com.feng.fengchat.common.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.feng.fengchat.common.user.domain.dto.WSChannelExtraDTO;
import com.feng.fengchat.common.user.domain.vo.resp.WSBaseResp;
import com.feng.fengchat.common.user.service.WebSocketService;
import com.feng.fengchat.common.user.service.adapter.WSAdapter;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jiangfeng
 * @date 2023/11/10
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    private static final Duration EXPIRE_TIME =Duration.ofHours(1);
    public static final int MAXIMUM_SIZE = 10000;
    private static final Cache<Integer,Channel> WAIT_LOGIN_MAP =Caffeine.newBuilder()
            .expireAfterWrite(EXPIRE_TIME)
            .maximumSize(MAXIMUM_SIZE)
            .build();
    public static final int MIN = 1;
    public static final int MAX = 10000;

    @Resource
    private WxMpService wxMpService;

    @Override
    public void handleLoginReq(Channel channel) throws WxErrorException {
        //获取随机场景code,并于channel关联
        Integer code = generateLoginCode(channel);
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) EXPIRE_TIME.getSeconds());
        //用适配器封装返回url
        sendMsg(channel, WSAdapter.buildLoginResp(wxMpQrCodeTicket));
    }

    @Override
    public void offLine(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
    }

    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel,new WSChannelExtraDTO());
    }

    /**
     * 获取唯一code码，并缓存到本地
     * @param channel
     * @return
     */
    private Integer generateLoginCode(Channel channel) {
        //todo 后续用分布式锁防止并发
        Integer code ;
        do{
            code = RandomUtil.randomInt(MIN, MAX);
        }while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code,channel)));
        return code;
    }

    private static void sendMsg(Channel channel, WSBaseResp resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }
}
