package com.feng.fengchat.common;

import com.feng.fengchat.common.common.annotation.RedissonLock;
import com.feng.fengchat.common.common.domain.enums.IdempotentEnum;
import com.feng.fengchat.common.common.domain.enums.ItemEnum;
import com.feng.fengchat.common.common.service.RedissonLockService;
import com.feng.fengchat.common.user.dao.UserDao;
import com.feng.fengchat.common.user.domain.entity.User;
import com.feng.fengchat.common.user.service.IUserBackpackService;
import com.feng.fengchat.common.user.service.LoginService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.feng.fengchat.common.common.config.ThreadPoolConfig.MALLCHAT_EXECUTOR;

/**
 *
 * @author jiangfeng
 * @date 2023/11/10
 */
@SpringBootTest
public class DaoTest {
    public static final long UID = 10005L;
    @Resource
    private UserDao userDao;

    @Resource
    private WxMpService wxMpService;

    @Resource
    private Redisson redisson;

    @Test
    public void test(){
        User user = userDao.getById(1);
        System.out.println(user);
        User insert = new User();
        insert.setName("11");
        insert.setOpenId("32");
        boolean save = userDao.save(insert);
        System.out.println(save);
    }

    @Test
    public void QRTest() throws WxErrorException {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 10000);
        System.out.println(wxMpQrCodeTicket.getUrl());
    }

    @Test
    public void redisTest(){

        redisson.getLock("test1").lock();
        System.out.println();
        redisson.getLock("test1").unlock();

    }

    @Resource
    @Qualifier(MALLCHAT_EXECUTOR)
    private ThreadPoolTaskExecutor executor;
    @Test
    public void threadPoolTest() throws InterruptedException {
        executor.execute(()->{
            if(true){
                throw new RuntimeException("报错了");
            }
        });
        Thread.sleep(200);

    }

    @Resource
    private LoginService loginService;
    @Test
    public void getToken(){
        System.out.println(loginService.login(10005L));
    }


    @Resource
    private IUserBackpackService userBackpackService;
    @Test
    public void acquireItem(){
        userBackpackService.acquireItem(UID, ItemEnum.PLANET.getId(), IdempotentEnum.UID,UID+"");
    }

    @Resource
    private RedissonLockService redissonLockService;

    @Test
    public void redissonLockTest() throws InterruptedException {
        redissonLockService.executeWithLock("redissonLockTest",()->{
            System.out.println("redissonLockTest");
        });

        System.out.println();

    }

    @Test
    @RedissonLock(prefixKey = "uid", key = "uid")
    public void redissonLockAnnoationTest() throws InterruptedException {
        System.out.println("redissonLockTest");
        System.out.println();
    }

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Test
    public void sendMQ() {
        String msg = "1235435";
        Message<String> build = MessageBuilder
                .withPayload(msg)
                .setHeader("KEYS",msg)
                .build();
        rocketMQTemplate.send("test-topic", build);
    }

}
