package com.feng.fengchat.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author jiangfeng
 * @date 2023/11/10
 */
@SpringBootApplication(scanBasePackages = "com.feng.fengchat")
@MapperScan("com.feng.fengchat.common.**.mapper")
public class FengchatApplication {
    public static void main(String[] args){
        SpringApplication.run(FengchatApplication.class,args);
    }
}
