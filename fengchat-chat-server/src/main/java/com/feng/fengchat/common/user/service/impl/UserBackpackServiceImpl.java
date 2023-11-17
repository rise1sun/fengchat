package com.feng.fengchat.common.user.service.impl;

import com.feng.fengchat.common.common.domain.enums.IdempotentEnum;
import com.feng.fengchat.common.common.domain.enums.YesOrNoEnum;
import com.feng.fengchat.common.common.utils.AssertUtil;
import com.feng.fengchat.common.user.dao.UserBackpackDao;
import com.feng.fengchat.common.user.domain.entity.UserBackpack;
import com.feng.fengchat.common.user.service.IUserBackpackService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 *
 * @author jiangfeng
 * @date 2023/11/17
 */
@Service
public class UserBackpackServiceImpl implements IUserBackpackService {
    @Resource
    private RedissonClient redisson;

    @Resource
    private UserBackpackDao userBackpackDao;
    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String business) {
        String idempotent =getAcquireItem(itemId,idempotentEnum,business);
        RLock lock = redisson.getLock(idempotent);
        boolean tryLock = lock.tryLock();
        AssertUtil.isTrue(tryLock,"请求太频繁啦");
        try{
            //校验是否已发放
            UserBackpack userBackpack = userBackpackDao.getIdempotent(idempotent);
            if (Objects.nonNull(userBackpack)) {
                return;
            }
            //发放物品
            UserBackpack backpack = UserBackpack.builder()
                    .itemId(itemId)
                    .uid(uid)
                    .idempotent(idempotent)
                    .status(YesOrNoEnum.NO.getStatus())
                    .build();
            userBackpackDao.save(backpack);
        }finally{
            lock.unlock();
        }

    }

    private String getAcquireItem(Long itemId, IdempotentEnum idempotentEnum, String business) {
        return String.format("%d_%d_%s",itemId,idempotentEnum.getCode(),business);
    }
}
