package com.pine.im.domain.mybatis;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: lipine
 * @Date: 2020/3/27 13:16
 * @Description: 数据库用户表
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class IMUser {
    private String uid;
    private String username;
    private String password;
    private String telephone;
    private int sex ;//1表示男0表示女
    private String email;//邮箱
}
