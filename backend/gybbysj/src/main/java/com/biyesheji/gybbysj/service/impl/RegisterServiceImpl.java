package com.biyesheji.gybbysj.service.impl;

import com.biyesheji.gybbysj.mapper.UserMapper;
import com.biyesheji.gybbysj.pojo.DTO.RegisterDTO;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public Result register(RegisterDTO registerDTO) {
        if(registerDTO.getEmail()==null){
            return Result.error("邮箱不能为空");
        }
        if(userMapper.searchUserByEmail(registerDTO.getEmail())!=null) {
            return Result.error("邮箱已存在");
        }
        if(registerDTO.getAccount()==null){
            return Result.error("用户名不能为空");
        }
        if(registerDTO.getPassword()==null){
            return Result.error("密码不能为空");
        }
        if(registerDTO.getPermission()==null){
            return Result.error("未知身份");
        }
        try {
            if(userMapper.register(registerDTO)==1) {
                return Result.success("注册成功");
            }
        }
        catch (Exception e) {
            return Result.error("账号名字已存在");
        }
        return Result.error("注册失败");
    }
}
