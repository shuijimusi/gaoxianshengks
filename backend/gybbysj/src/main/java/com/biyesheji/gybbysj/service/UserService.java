package com.biyesheji.gybbysj.service;

import com.biyesheji.gybbysj.pojo.DTO.UserLoginDTO;
import com.biyesheji.gybbysj.pojo.Result;

public interface UserService {
    Result Login(UserLoginDTO userLoginDTO);
}
