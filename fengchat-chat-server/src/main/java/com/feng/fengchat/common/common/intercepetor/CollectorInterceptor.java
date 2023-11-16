package com.feng.fengchat.common.common.intercepetor;

import cn.hutool.extra.servlet.ServletUtil;
import com.feng.fengchat.common.common.domain.dto.RequestInfo;
import com.feng.fengchat.common.common.utils.RequestHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 *
 * @author jiangfeng
 * @date 2023/11/16
 */
@Component
public class CollectorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestInfo requestInfo = new RequestInfo();
        Long uid = Optional.ofNullable(request.getAttribute(TokenIntercepetor.UID))
                .map(Object::toString)
                .map(Long::parseLong)
                .orElse(null);
        requestInfo.setUid(uid);
        requestInfo.setIp(ServletUtil.getClientIP(request));
        RequestHolder.set(requestInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
    }
}
