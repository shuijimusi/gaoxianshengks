package com.biyesheji.gybbysj.controller;

import com.biyesheji.gybbysj.pojo.Result;
import com.sun.source.tree.RequiresTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/demo")
public class demoController {
    @GetMapping("/get")
    public Result get(){
        return null;
    }
}
