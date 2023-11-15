package com.feng.fengchat.common.common.intercepetor;

import com.feng.fengchat.common.common.config.TokenIntercepetorConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 *
 * @author jiangfeng
 * @date 2023/11/15
 */
@Component
public class HandlerInterceptor implements WebMvcConfigurer {

    @Resource
    private TokenIntercepetorConfig tokenIntercepetorConfig;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenIntercepetorConfig)
                .addPathPatterns("/capi/**");
    }
}
