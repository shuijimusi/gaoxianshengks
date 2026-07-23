package com.biyesheji.gybbysj.controller;

import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.ExamProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/examProcess")
@RestController
public class ExamProcessController {
    @Autowired
    private ExamProcessService examProcessService;


    //考试查询所有题目的接口
    @PostMapping
    public Result searchExamQuestion(@RequestParam Long examId){
        return examProcessService.searchExamQuestion(examId);
    }


    //缓存题目
    @PostMapping("/cacheQuestion")
    public Result cacheQuestion(@RequestBody Map<String, Object> data) {
        return examProcessService.cacheQuestion(data);
    }


    //完整提交试卷
    @PostMapping("/submitExam")
    public Result submitExam(@RequestBody Map<String, Object> data) {
        return examProcessService.submitExam(data);
    }
}
