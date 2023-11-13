package com.feng.fengchat.common.user.service;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 *
 * @author jiangfeng
 * @date 2023/11/13
 */
public interface WXMsgService {

    /**
     * 扫码
     * @param wxMpXmlMessage
     * @param wxMpService
     */
    WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage, WxMpService wxMpService);

    void authorize(WxOAuth2UserInfo userInfo);
}
