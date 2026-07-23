package com.biyesheji.gybbysj.controller;

import com.biyesheji.gybbysj.pojo.DTO.Question.QuestionAddDTO;
import com.biyesheji.gybbysj.pojo.DTO.Question.QuestionSearchDTO;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService QuestionService;

    // 查询所有问题数量
    @GetMapping("/returnallquestion")
    public Result returnAllQuestion(){
        return QuestionService.returnAllQuestion();
    }
    //条件查询所有问题数量
    @PostMapping
    public Result returnAllQuestionByCondition(@RequestBody QuestionSearchDTO questionSearchDTO){
        return QuestionService.returnAllQuestionByCondition(questionSearchDTO);
    }

    // 添加问题
    @PostMapping("/add")
    public Result question(@RequestBody QuestionAddDTO questionAddDTO){
        return QuestionService.addQuestion(questionAddDTO);
    }


    // 删除问题
    @PostMapping("/delete")
    public Result deleteQuestion(@RequestParam Long questionId){
        return QuestionService.deleteQuestion(questionId);
    }

    // 修改问题
    @PostMapping("/update")
    public Result updateQuestion(@RequestBody QuestionAddDTO questionAddDTO){
        return QuestionService.updateQuestion(questionAddDTO);
    }


    //根据id查问题具体信息
    @PostMapping("/getoneimage")
    public Result getOneImage(@RequestParam Long questionId){
        return QuestionService.getOneImage(questionId);
    }
}
