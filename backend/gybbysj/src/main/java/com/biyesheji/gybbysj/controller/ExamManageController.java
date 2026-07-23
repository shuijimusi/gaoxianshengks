package com.biyesheji.gybbysj.controller;

import com.biyesheji.gybbysj.pojo.DTO.ExamManage.*;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.ExamManageService;
import com.biyesheji.gybbysj.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exam")
public class ExamManageController {
    @Autowired
    private ExamManageService examManageService;

    // 获取试卷列表
    @PostMapping("/page")
    public Result exam(@RequestBody GetPageListDTO getPageListDTO){
        return examManageService.getExamList(getPageListDTO);
    }

    // 删除考试
    @PostMapping("/delete")
    public Result deleteExam(@RequestParam Long paperId){
        return examManageService.deleteExam(paperId);
    }

    //查看考试详情

    @PostMapping("/detail")
    public Result detail(@RequestParam Long paperId){
        GetExamDetailDTO getExamDetailDTO = new GetExamDetailDTO();
        getExamDetailDTO.setExamid(paperId);
        return examManageService.getExamDetail(getExamDetailDTO);
    }

    //发布考试
    @PostMapping("/start")
    public Result startExam(@RequestBody startExamDTO startExamDTO){
        return examManageService.startExam(startExamDTO);
    }


    //所有考试一览
    @PostMapping("/allteacher")
    public Result allExam(@RequestBody GetAllExamDTO getAllExamDTO){
        getAllExamDTO.setUserid(UserContext.getUser().getUserId());
        return examManageService.getAllExamList(getAllExamDTO);
    }
}
