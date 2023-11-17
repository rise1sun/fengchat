package com.feng.fengchat.common.user.service;

import com.feng.fengchat.common.common.domain.enums.IdempotentEnum;
import com.feng.fengchat.common.user.domain.entity.UserBackpack;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户背包表 服务类
 * </p>
 *
 * @author jiangfeng
 * @since 2023-11-15
 */
public interface IUserBackpackService{

    /**
     *
     * @param uid
     * @param itemId
     * @param idempotentEnum
     * @param busies
     */
     void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String busies);

}
