package com.feng.fengchat.common.common.service;

import com.feng.fengchat.common.common.domain.enums.YesOrNoEnum;
import com.feng.fengchat.common.common.utils.AssertUtil;
import com.feng.fengchat.common.user.domain.entity.UserBackpack;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author jiangfeng
 * @date 2023/11/17
 */
@Service
public class RedissonLockService{

    @Resource
    private RedissonClient redisson;

    public <T> T executeWithLock(String idempotent, Integer waitTime, TimeUnit timeUnit, SupplierThrow<T> supplier)  throws Throwable {
        RLock lock = redisson.getLock(idempotent);
        boolean tryLock = lock.tryLock(waitTime,timeUnit);
        AssertUtil.isTrue(tryLock,"请求太频繁啦");
        try{
          return supplier.get();
        }finally{
            lock.unlock();
        }
    }

    public <T> T executeWithLock(String idempotent, SupplierThrow<T> supplier) throws Throwable {
        return executeWithLock(idempotent, -1, TimeUnit.MILLISECONDS, supplier);
    }

    public void executeWithLock(String idempotent, Runnable runnable) throws InterruptedException {
        RLock lock = redisson.getLock(idempotent);
        boolean tryLock = lock.tryLock();
        AssertUtil.isTrue(tryLock,"请求太频繁啦");
        try{
             runnable.run();
        }finally{
            lock.unlock();
        }
    }

    @FunctionalInterface
    public interface SupplierThrow<T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T get() throws Throwable;
    }
}
