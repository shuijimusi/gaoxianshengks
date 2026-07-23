package com.biyesheji.gybbysj.controller;

import com.biyesheji.gybbysj.pojo.DTO.RegisterDTO;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private RegisterService registerservice;
    // 注册
    @PostMapping
    public Result register(@RequestBody RegisterDTO registerDTO) {
        return registerservice.register(registerDTO);
    }
}
