package com.biyesheji.gybbysj.controller;

import com.biyesheji.gybbysj.pojo.DTO.UserLoginDTO;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;
    @PostMapping
    public Result Login(@RequestBody UserLoginDTO userLoginDTO){
        return userService.Login(userLoginDTO);
    }
}
