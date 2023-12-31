package com.feng.fengchat.common.user.dao;

import com.feng.fengchat.common.common.domain.enums.ItemEnum;
import com.feng.fengchat.common.common.domain.enums.ItemTypeEnum;
import com.feng.fengchat.common.user.domain.entity.ItemConfig;
import com.feng.fengchat.common.user.mapper.ItemConfigMapper;
import com.feng.fengchat.common.user.service.IItemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 功能物品配置表 服务实现类
 * </p>
 *
 * @author jiangfeng
 * @since 2023-11-15
 */
@Service
public class ItemConfigDao extends ServiceImpl<ItemConfigMapper, ItemConfig>{

    public List<ItemConfig> getByType(Integer type) {
        return lambdaQuery().eq(ItemConfig::getType, type).list();
    }

    public ItemConfig vaildById(Long itemId) {
        return lambdaQuery().eq(ItemConfig::getType, ItemTypeEnum.BADGE.getType())
                        .eq(ItemConfig::getId,itemId).one();
    }
}
