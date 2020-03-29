package com.pine.im.service;

import com.pine.im.domain.mybatis.IMUser;
import com.pine.im.domain.mybatis.IMUserMapper;
import com.pine.im.service.impl.IMUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author: lipine
 * @Date: 2020/3/27 14:26
 * @Description: 用户service
 * @Version 1.0
 */
@Component
public class IMUserService implements IMUserServiceImpl {

    @Autowired
    private IMUserMapper userMapper;

    @Override
    public String getLoginResponse(String username, String password) {
        IMUser imUser = userMapper.findByUserName(username);
        if(imUser != null){
            if(imUser.getPassword().equals(password)){
                return imUser.getUid();
            }
        }
        return null;
    }

    @Override
    public IMUser getByUid(String uid) {
        return null;
    }
}
