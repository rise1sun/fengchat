package com.feng.fengchat.common.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.feng.fengchat.common.common.event.UserOnlineEvent;
import com.feng.fengchat.common.user.dao.UserDao;
import com.feng.fengchat.common.websocket.NettyUtils;
import com.feng.fengchat.common.websocket.domain.dto.WSChannelExtraDTO;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.websocket.domain.vo.resp.WSBaseResp;
import com.feng.fengchat.common.user.service.LoginService;
import com.feng.fengchat.common.user.service.UserService;
import com.feng.fengchat.common.user.service.WebSocketService;
import com.feng.fengchat.common.user.service.adapter.WSAdapter;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
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

    @Resource
    private UserService userService;

    @Resource
    private UserDao userDao;

    @Resource
    private LoginService loginService;

    @Resource
    ApplicationEventPublisher applicationEventPublisher;

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

    @Override
    public void waitAuth(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if(ObjectUtil.isNull(channel)){
            return;
        }
        sendMsg(channel,WSAdapter.buildWaitAuthResp());

    }

    @Override
    public void authSuccess(Integer code, Long uid) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if(ObjectUtil.isNull(channel)){
            return;
        }

        User user = userDao.getById(uid);
        String token = userService.login(uid);
        WAIT_LOGIN_MAP.invalidate(code);
        loginSuccess(channel,user,token);
    }

    @Override
    public void handleAuthSuccess(Channel channel,String token) {
        Long uid = loginService.getValidUid(token);
        if(Objects.nonNull(uid)){
            User user = userDao.getById(uid);
            //登录成功
            loginSuccess(channel,user,token);
        }else{
            //token失效
            sendMsg(channel,WSAdapter.buildInvolidTokenResp());
        }




    }

    private void loginSuccess(Channel channel, User user, String taken) {
        //登录成功，绑定channel和uid关系
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());

        sendMsg(channel,WSAdapter.buildLoginSuccessResp(user,taken));

        user.setLastOptTime(new Date());
        user.refreshIp(NettyUtils.getAttr(channel, NettyUtils.IP));
        applicationEventPublisher.publishEvent(new UserOnlineEvent(this, user));
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
