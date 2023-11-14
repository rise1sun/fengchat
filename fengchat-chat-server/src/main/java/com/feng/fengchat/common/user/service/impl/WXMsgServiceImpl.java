package com.feng.fengchat.common.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.feng.fengchat.common.user.dao.UserDao;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.service.UserService;
import com.feng.fengchat.common.user.service.WXMsgService;
import com.feng.fengchat.common.user.service.WebSocketService;
import com.feng.fengchat.common.user.service.adapter.TextBuilder;
import com.feng.fengchat.common.user.service.adapter.UserAdapter;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jiangfeng
 * @date 2023/11/13
 */
@Slf4j
@Service
public class WXMsgServiceImpl implements WXMsgService {

    /**
     * 用户的openId和前端登录场景code的映射关系
     */
    private static final ConcurrentHashMap<String,Integer> WAIT_AUTH_MAP= new ConcurrentHashMap<>();


    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    @Value("${wx.mp.callback}")
    private String callback;

    @Resource
    private UserDao userDao;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private WebSocketService webSocketService;

    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage, WxMpService wxMpService) {
        String openId = wxMpXmlMessage.getFromUser();
        Integer code = Integer.valueOf(getEventkey(wxMpXmlMessage));
        User user = userDao.getUserByOpenId(openId);
        boolean registered = Objects.nonNull(user);
        if(registered && StrUtil.isNotBlank(user.getAvatar())){
            //直接登录成功
            return null;
        }
        if(!registered){
            //注册
            User insert = UserAdapter.buildUser(openId);
            userService.registered(insert);

        }
        WAIT_AUTH_MAP.put(openId,code);
        webSocketService.waitAuth(code);
        String AuthUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback+"/wx/portal/public/callBack"));
        return new TextBuilder().build("请点击登录：<a href=\""+AuthUrl+"\">"+"登录</a>",wxMpXmlMessage,wxMpService);
    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        User user = userDao.getUserByOpenId(userInfo.getOpenid());
        String openid = userInfo.getOpenid();
        if (StrUtil.isEmpty(user.getName())) {
            updateUser(userInfo, user.getId());
        }
        webSocketService.authSuccess(WAIT_AUTH_MAP.get(openid), user.getId());
    }

    private void updateUser(WxOAuth2UserInfo userInfo, Long uid) {
        User buildAuthUser = UserAdapter.buildAuthUser(userInfo, uid);
        for(int i = 0; i < 5; i++) {
            try {
                userDao.updateById(buildAuthUser);
                return;
            }  catch (DuplicateKeyException e) {
                log.info("updateUser userInfo duplicate uid:{},info:{}", uid, userInfo);
            } catch (Exception e) {
                log.error("updateUser userInfo fail uid:{},info:{}", uid, userInfo);
            }
            buildAuthUser.setName("名字重置" + RandomUtil.randomInt(100000));
        }
    }

    private static String getEventkey(WxMpXmlMessage wxMpXmlMessage) {
        String eventKey = wxMpXmlMessage.getEventKey();
        String qrscene_ = eventKey.replace("qrscene_", "");
        return qrscene_;
    }
}
