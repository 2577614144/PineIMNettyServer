package com.pine.im.service.impl;

import com.pine.im.domain.mybatis.IMUser;

/**
 * @Author: lipine
 * @Date: 2020/3/27 14:26
 * @Description:
 * @Version 1.0
 */
public interface IMUserServiceImpl {

    String getLoginResponse(String username,String password);

    IMUser getByUid(String uid);
}
