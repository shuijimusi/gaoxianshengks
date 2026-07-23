package com.biyesheji.gybbysj.mapper;


import com.biyesheji.gybbysj.pojo.Entity.User;
import com.biyesheji.gybbysj.pojo.DTO.UserLoginDTO;
import org.apache.ibatis.annotations.Select;


public interface LoginMapper {
    @Select("select * from user where username=#{account} and password=#{password}")
    User login(UserLoginDTO userLoginDTO);
}
