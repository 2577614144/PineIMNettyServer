package com.pine.im;

import com.pine.im.domain.mybatis.IMUser;
import com.pine.im.domain.mybatis.IMUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * @Author: lipine
 * @Date: 2020/3/27 13:30
 * @Description: 用户表sql测试
 * @Version 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class IMUserMapperTest {

    @Autowired
    private IMUserMapper userMapper;

    @Test
    @Rollback
    public void test() throws Exception {
        String uid = UUID.randomUUID().toString();
        IMUser imUser = new IMUser();
        imUser.setUid(uid);
        imUser.setUsername("xiaoli");
        imUser.setPassword("123456");
        imUser.setEmail("liqingsong@css.com.cn");
        imUser.setSex(1);
        imUser.setTelephone("15911056460");
        userMapper.insertByIMUser(imUser);
        IMUser u = userMapper.findByUid(uid);
        System.out.println(u.toString());
    }
}
