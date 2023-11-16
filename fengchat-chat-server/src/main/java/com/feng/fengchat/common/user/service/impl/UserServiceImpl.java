package com.feng.fengchat.common.user.service.impl;

import com.feng.fengchat.common.common.constant.RedisKey;
import com.feng.fengchat.common.common.domain.enums.ItemEnum;
import com.feng.fengchat.common.common.utils.AssertUtil;
import com.feng.fengchat.common.common.utils.JwtUtils;
import com.feng.fengchat.common.common.utils.RedisUtils;
import com.feng.fengchat.common.user.dao.UserBackpackDao;
import com.feng.fengchat.common.user.dao.UserDao;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.domain.entity.UserBackpack;
import com.feng.fengchat.common.user.domain.vo.resp.UserInfoResp;
import com.feng.fengchat.common.user.service.UserService;
import com.feng.fengchat.common.user.service.adapter.UserAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

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

    @Override
    @Transactional
    public Long registered(User insert) {
        userDao.save(insert);
        //todo 注册监听

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

    private String getUserTokenKey(Long id) {
        String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, id);
        return key;
    }
}
