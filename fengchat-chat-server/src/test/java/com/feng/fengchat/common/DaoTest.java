package com.feng.fengchat.common;

import com.feng.fengchat.common.common.utils.RedisUtils;
import com.feng.fengchat.common.user.dao.UserDao;
import com.feng.fengchat.common.user.domain.entity.User;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

import static com.feng.fengchat.common.common.config.ThreadPoolConfig.MALLCHAT_EXECUTOR;

/**
 *
 * @author jiangfeng
 * @date 2023/11/10
 */
@SpringBootTest
public class DaoTest {
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
}
