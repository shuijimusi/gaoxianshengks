package com.biyesheji.gybbysj.service.impl;

import com.biyesheji.gybbysj.pojo.Entity.User;
import com.biyesheji.gybbysj.mapper.LoginMapper;
import com.biyesheji.gybbysj.pojo.DTO.UserImageDTO;
import com.biyesheji.gybbysj.pojo.DTO.UserLoginDTO;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper;

    public UserServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Result Login(UserLoginDTO userLoginDTO) {
        User user = loginMapper.login(userLoginDTO);
        UserImageDTO userImageDTO = new UserImageDTO();
        if (user == null)
        {
            return Result.error("用户名或密码错误");
        }
        userImageDTO.setUserId(user.getId());
        userImageDTO.setUsername(user.getUsername());
        userImageDTO.setRole(user.getRole());
        String token = UUID.randomUUID().toString().replace("-", "");
        String redisKey = "login:token:" + token;

        String userJson;
        try {
            userJson = objectMapper.writeValueAsString(userImageDTO);
        } catch (Exception e) {
            log.error("用户信息序列化失败", e);
            return Result.error("登录异常，请重试");
        }
        stringRedisTemplate.opsForValue().set(redisKey, userJson, Duration.ofHours(2));
        log.info("登录成功，生成token：{}",token);
        return Result.success(token);
    }

}
