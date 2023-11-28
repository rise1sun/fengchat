package com.feng.fengchat.transaction.dao;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.fengchat.transaction.domain.entity.SecureInvokeRecord;
import com.feng.fengchat.transaction.mapper.SecureInvokeRecordMapper;
import com.feng.fengchat.transaction.service.SecureInvokeService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 *
 * @author jiangfeng
 * @date 2023/11/28
 */
@Component
public class SecureInvokeRecordDao extends ServiceImpl<SecureInvokeRecordMapper, SecureInvokeRecord> {
  
    public List<SecureInvokeRecord> getWaitRetryRecords() {
        //查2分钟前的失败数据。避免刚入库的数据被查出来
        Date now =new Date();
        Date afterTime = DateUtil.offsetMinute(now, (int) SecureInvokeService.RETRY_INTERVAL_MINUTES);
        return lambdaQuery()
                .eq(SecureInvokeRecord::getStatus, SecureInvokeRecord.STATUS_WAIT)
                .lt(SecureInvokeRecord::getNextRetryTime, new Date())
                .lt(SecureInvokeRecord::getCreateTime, afterTime)
                .list();
    }
    
   
}
