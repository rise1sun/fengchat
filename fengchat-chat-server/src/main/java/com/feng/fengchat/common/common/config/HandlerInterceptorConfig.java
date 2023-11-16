package com.feng.fengchat.common.common.config;

import com.feng.fengchat.common.common.intercepetor.CollectorInterceptor;
import com.feng.fengchat.common.common.intercepetor.TokenIntercepetor;
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
public class HandlerInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private TokenIntercepetor tokenIntercepetor;

    @Resource
    private CollectorInterceptor collectorInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenIntercepetor)
                .addPathPatterns("/capi/**");

        registry.addInterceptor(collectorInterceptor)
                .addPathPatterns("/capi/**");
    }
}
