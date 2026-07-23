package com.biyesheji.gybbysj.controller;

import com.biyesheji.gybbysj.pojo.DTO.PageCreate.AddQuestionDTO;
import com.biyesheji.gybbysj.pojo.DTO.PageCreate.PageCreateDTO;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.PageCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page")
public class PageCreateController {
    @Autowired
    private PageCreateService pageCreateService;
    @PostMapping("/selectquestion")
    public Result selectQuestion(@RequestBody AddQuestionDTO addQuestionDTO){
        return pageCreateService.selectQuestion(addQuestionDTO);
    }

    // 创建试卷
    @PostMapping("/createpaper")
    public Result createPaper(@RequestBody PageCreateDTO pageCreateDTO){
        return pageCreateService.createPaper(pageCreateDTO);
    }


}
