package com.feng.fengchat.common.common.domain.vo;

import com.feng.fengchat.common.common.domain.vo.response.ApiResult;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.Charsets;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jiangfeng
 * @date 2023/11/15
 */
@AllArgsConstructor
public enum HttpRequestErrorEnum {

    UNAUTHORIZED(401, "Unauthorized");

    private Integer code;

    private String message;


    public void sendError(HttpServletResponse response) throws Exception {
       // response.setStatus(this.code);
        response.setContentType(Charsets.UTF_8.toString());
        response.getWriter().write(ApiResult.fail(this.code,this.message).toString() );
    }

}
