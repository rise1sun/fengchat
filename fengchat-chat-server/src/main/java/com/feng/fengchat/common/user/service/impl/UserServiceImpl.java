package com.feng.fengchat.common.user.service.impl;

import com.feng.fengchat.common.common.annotation.RedissonLock;
import com.feng.fengchat.common.common.cache.ItemCache;
import com.feng.fengchat.common.common.constant.RedisKey;
import com.feng.fengchat.common.common.domain.enums.ItemEnum;
import com.feng.fengchat.common.common.domain.enums.ItemTypeEnum;
import com.feng.fengchat.common.common.event.UserRegisteEvent;
import com.feng.fengchat.common.common.utils.AssertUtil;
import com.feng.fengchat.common.common.utils.JwtUtils;
import com.feng.fengchat.common.common.utils.RedisUtils;
import com.feng.fengchat.common.user.dao.UserBackpackDao;
import com.feng.fengchat.common.user.dao.UserDao;
import com.feng.fengchat.common.user.domain.entity.ItemConfig;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.domain.entity.UserBackpack;
import com.feng.fengchat.common.user.domain.vo.resp.BadgeInfoResp;
import com.feng.fengchat.common.user.domain.vo.resp.UserInfoResp;
import com.feng.fengchat.common.user.service.UserService;
import com.feng.fengchat.common.user.service.adapter.UserAdapter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author jiangfeng
 * @date 2023/11/13
 */
@Service
public class UserServiceImpl implements UserService {

    private static final long TOKEN_EXPIRE_DAYS = 3;
    @Resource
    private UserDao userDao;

    @Resource
    private UserBackpackDao userBackpackDao;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private ItemCache itemCache;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public Long registered(User insert) {
        userDao.save(insert);
        applicationEventPublisher.publishEvent(new UserRegisteEvent(this,insert));
        return insert.getId();
    }

    @Override
    public String login(Long id) {
        String token = jwtUtils.createToken(id);
        RedisUtils.set(getUserTokenKey(id), token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer countModfiyNameCard = userBackpackDao.countModfiyNameCard(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        UserInfoResp rep =UserAdapter.buildUserInfoResp(user,countModfiyNameCard);
        return rep;
    }

    @Override

    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public void modifyName(Long uid,String name) {
        //校验名字是否重复
        User user = userDao.getUserByName(name);
        AssertUtil.isEmpty(user,"名字已存在,请换一个噢");
        //校验是否有改名卡
        UserBackpack userBackpack = userBackpackDao.getModfiyNameCard(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(userBackpack,"没有改名卡了，快去商城购买吧");
        //使用改名卡
        Boolean success = userBackpackDao.userModifyNameCard(userBackpack.getId());
        if(success){
            userDao.modifyName(uid,name);
        }
    }

    @Override
    public List<BadgeInfoResp> getBadgeList(Long uid) {
        //查询所有徽章
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        Set<Long> collect = itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toSet());
        //查询用户拥有的徽章
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uid,collect);
        //查询用户当前佩戴的标签
        User user = userDao.getById(uid);
        return UserAdapter.buildBadgeResp(itemConfigs, backpacks, user);
    }

    @Override
    public void wearingBadge(Long itemId, Long uid) {
        UserBackpack userBackpack = userBackpackDao.getVaildById(itemId,uid);
        AssertUtil.isEmpty(userBackpack,"背包没有徽章噢");
        ItemConfig itemConfig =itemCache.getById(itemId);
        AssertUtil.isEmpty(itemConfig,"佩戴的不是徽章噢");

        userDao.wearingBadge(itemId,uid);

    }

    private String getUserTokenKey(Long id) {
        String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, id);
        return key;
    }
}
