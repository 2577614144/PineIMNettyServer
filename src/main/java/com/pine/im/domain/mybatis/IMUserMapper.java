package com.pine.im.domain.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @Author: lipine
 * @Date: 2020/3/27 13:24
 * @Description: 用户表Mapper
 * @Version 1.0
 */
@Mapper
@Component
public interface IMUserMapper {
    /**
     * 通过用户id查找用户信息
     * @param uid
     * @return
     */
    @Select("SELECT * FROM im_user WHERE UID = #{uid}")
    IMUser findByUid(@Param("uid") String uid);


    /**
     * 通过用户名查找用户信息
     * @param username
     * @return
     */
    @Select("SELECT * FROM im_user WHERE USERNAME = #{username}")
    IMUser findByUserName(@Param("username") String username);

    /**
     * 插入uid、username、password
     * @param uid
     * @param username
     * @param password
     * @return
     */
    @Insert("INSERT INTO im_user(UID,USERNAME,PASSWORD) VALUES(#{uid},#{username}, #{password})")
    int insert(@Param("uid") String uid,@Param("username") String username, @Param("password") String password);

    /**
     * 插入IMUser对象
     * @param imUser
     * @return
     */
    @Insert("INSERT INTO im_user(UID,USERNAME,PASSWORD,TELEPHONE,SEX,EMAIL) VALUES(#{uid},#{username}, #{password}, #{telephone}, #{sex}, #{email})")
    int insertByIMUser(IMUser imUser);
}
