package com.feng.fengchat.common.user.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author jiangfeng
 * @date 2023/11/20
 */
@Data
public class IpDetail implements Serializable {
    private static final long serialVersionUID = 7448791014893241555L;
    //注册时的ip
    private String ip;
    //最新登录的ip
    private String isp;
    private String isp_id;
    private String city;
    private String city_id;
    private String country;
    private String country_id;
    private String region;
    private String region_id;
}
