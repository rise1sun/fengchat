package com.feng.fengchat.common.user.dao;

import com.feng.fengchat.common.common.domain.enums.YesOrNoEnum;
import com.feng.fengchat.common.user.domain.entity.UserBackpack;
import com.feng.fengchat.common.user.mapper.UserBackpackMapper;
import com.feng.fengchat.common.user.service.IUserBackpackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author jiangfeng
 * @since 2023-11-15
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack> {

    public Integer countModfiyNameCard(Long uid, Long userBackpackId) {
        return lambdaQuery().eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, userBackpackId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .count();
    }

    public UserBackpack getModfiyNameCard(Long uid, Long itemId) {
        return lambdaQuery().eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .one();
    }

    public Boolean userModifyNameCard(Long id) {
        return lambdaUpdate().eq(UserBackpack::getId, id)
                        .set(UserBackpack::getStatus, YesOrNoEnum.YES.getStatus())
                        .update();

    }

    public List<UserBackpack> getByItemIds(Long uid, Set<Long> collect) {
        return lambdaQuery().eq(UserBackpack::getUid, uid)
                .in(UserBackpack::getItemId, collect)
                .eq(UserBackpack::getStatus,YesOrNoEnum.NO.getStatus())
                .list();
    }

    public UserBackpack getVaildById(Long itemId,Long uid) {
        return lambdaQuery().eq(UserBackpack::getId,itemId)
                        .eq(UserBackpack::getStatus,YesOrNoEnum.NO.getStatus())
                        .eq(UserBackpack::getUid,uid)
                        .one();
    }

    public UserBackpack getIdempotent(String idempotent) {
        return lambdaQuery().eq(UserBackpack::getIdempotent,idempotent)
                        .one();
    }
}
