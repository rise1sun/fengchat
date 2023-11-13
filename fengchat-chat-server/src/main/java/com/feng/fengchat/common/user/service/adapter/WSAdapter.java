package com.feng.fengchat.common.user.service.adapter;

import com.feng.fengchat.common.user.domain.vo.enums.WSRespTypeEnum;
import com.feng.fengchat.common.user.domain.vo.resp.WSBaseResp;
import com.feng.fengchat.common.user.domain.vo.resp.WSLoginUrl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Component;

/**
 *
 * @author jiangfeng
 * @date 2023/11/13
 */
@Component
public class WSAdapter {


    public static WSBaseResp buildLoginResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> wsLoginUrlWSBaseResp = new WSBaseResp<>();
        wsLoginUrlWSBaseResp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        wsLoginUrlWSBaseResp.setDate(WSLoginUrl.builder().loginUrl(wxMpQrCodeTicket.getUrl()).build());
        return wsLoginUrlWSBaseResp;
    }
}
