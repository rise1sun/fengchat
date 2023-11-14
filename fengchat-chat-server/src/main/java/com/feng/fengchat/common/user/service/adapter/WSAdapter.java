package com.feng.fengchat.common.user.service.adapter;

import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.domain.vo.enums.WSRespTypeEnum;
import com.feng.fengchat.common.user.domain.vo.resp.WSBaseResp;
import com.feng.fengchat.common.user.domain.vo.resp.WSLoginSuccess;
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

    public static WSBaseResp buildWaitAuthResp() {
        WSBaseResp<WSLoginUrl> wsLoginUrlWSBaseResp = new WSBaseResp<>();
        wsLoginUrlWSBaseResp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return  wsLoginUrlWSBaseResp;
    }

    public static WSBaseResp buildAuthSuccessResp(String taken) {
        WSBaseResp<WSLoginSuccess> wsLoginSuccessWSBaseResp = new WSBaseResp<>();
        wsLoginSuccessWSBaseResp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        wsLoginSuccessWSBaseResp.setDate(WSLoginSuccess.builder().token(taken).build());
        return  wsLoginSuccessWSBaseResp;
    }

    public static WSBaseResp buildLoginSuccessResp(User user, String taken) {
        WSLoginSuccess wsLoginSuccess = new WSLoginSuccess();
        wsLoginSuccess.setAvatar(user.getAvatar());
        wsLoginSuccess.setName(user.getName());
        wsLoginSuccess.setToken(taken);
        wsLoginSuccess.setUid(user.getId());

        WSBaseResp<WSLoginSuccess> wsLoginSuccessWSBaseResp = new WSBaseResp<>();
        wsLoginSuccessWSBaseResp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        wsLoginSuccessWSBaseResp.setDate(wsLoginSuccess);
        return wsLoginSuccessWSBaseResp;
    }
}
