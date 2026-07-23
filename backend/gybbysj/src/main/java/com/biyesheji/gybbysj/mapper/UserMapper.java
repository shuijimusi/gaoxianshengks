package com.biyesheji.gybbysj.mapper;

import com.biyesheji.gybbysj.pojo.Entity.User;
import com.biyesheji.gybbysj.pojo.DTO.RegisterDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;


public interface UserMapper {
    
    // 根据邮箱查询用户
    @Select("select * from user where email=#{email}")
    User searchUserByEmail(String email);
    //注册
    @Insert("insert into user(username,password,email,role) values(#{account},#{password},#{email},#{permission})")
    int register(RegisterDTO registerDTO);
}
