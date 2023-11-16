package com.feng.fengchat.common.common.intercepetor;

import com.feng.fengchat.common.common.domain.vo.HttpRequestErrorEnum;
import com.feng.fengchat.common.user.service.LoginService;
import org.apache.commons.codec.Charsets;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author jiangfeng
 * @date 2023/11/15
 */
@Component
public class TokenIntercepetor implements HandlerInterceptor {
    public static final String HEADERS_AUTHORIZATION = "Authorization";
    public static final String BEARER_ = "Bearer ";
    public static final String UID = "uid";
    public static final String PUBLIC = "public";
    private static final int PBULIC_LENGTH = 3;
    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        Long validUid = loginService.getValidUid(token);
        //用户已登录
        if(Objects.nonNull(validUid) ){
            request.setAttribute(UID,validUid);
        }else{
            //用户未登录
            //校验接口是否需要登录
            String requestURI = request.getRequestURI();
            boolean isPublicURI = isPublicURI(requestURI);
            if(!isPublicURI){
                HttpRequestErrorEnum.UNAUTHORIZED.sendError(response);
                return false;
            }
        }
        return true;
    }

    private boolean isPublicURI(String requestURI) {
        String[] split = requestURI.split("/");
        if(split.length > PBULIC_LENGTH&& PUBLIC.equals(split[PBULIC_LENGTH])){
            return true;
        }
        return false;
    }

    private String getToken(HttpServletRequest request) {
        String BEARER_TOKEN = request.getHeader(HEADERS_AUTHORIZATION).toString();
        Optional<String> token = Optional.ofNullable(Optional.ofNullable(BEARER_TOKEN)
                .filter(t -> t.startsWith(BEARER_))
                .map(t -> t.replaceFirst(BEARER_, ""))
                .orElse(null));
        return token.get();


    }
}
