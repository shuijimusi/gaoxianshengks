package com.biyesheji.gybbysj.controller;

import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.util.UserContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/getpermission")
    public Result getPermission() {
        return Result.success(UserContext.getUser().getRole());
    }
}
