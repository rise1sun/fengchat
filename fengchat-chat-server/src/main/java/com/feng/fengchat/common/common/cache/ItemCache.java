package com.feng.fengchat.common.common.cache;

import com.feng.fengchat.common.user.dao.ItemConfigDao;
import com.feng.fengchat.common.user.domain.entity.ItemConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author jiangfeng
 * @date 2023/11/17
 */
@Component
public class ItemCache {

    @Resource
    private ItemConfigDao itemConfigDao;

    @Cacheable(value = "itemConfig", key = "'itemConfig:'+#type")
    public List<ItemConfig> getByType(Integer type) {
        List<ItemConfig> list =itemConfigDao.getByType(type);
        return list;
    }
}
