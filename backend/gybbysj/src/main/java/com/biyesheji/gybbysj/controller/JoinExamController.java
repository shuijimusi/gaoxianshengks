package com.biyesheji.gybbysj.controller;

import com.biyesheji.gybbysj.pojo.DTO.joinExam.FindAllExamDTO;
import com.biyesheji.gybbysj.pojo.DTO.joinExam.FindClassDTO;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.JoinExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/joinexam")
public class JoinExamController {
    @Autowired
    private JoinExamService joinExamService;
    //获取所有考试
    @PostMapping
    public Result joinExam(@RequestBody FindAllExamDTO findAllExamDTO){
        return  joinExamService.findAllExam(findAllExamDTO);
    }

    //查询当前学生所在的班级
    @PostMapping("/class")
    public Result findClass(@RequestBody FindClassDTO findClassDTO){
        return joinExamService.findClass(findClassDTO);
    }
}
