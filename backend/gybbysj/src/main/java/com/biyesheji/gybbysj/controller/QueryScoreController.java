package com.biyesheji.gybbysj.controller;

import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindAllClassDTO;
import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindExamListDTO;
import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindExamStudentListDTO;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.QueryScoreService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/queryScore")
public class QueryScoreController {
    @Autowired
    private QueryScoreService queryScoreService;
    @PostMapping("/status")
    public Result queryScoreStatus(@RequestParam Long examId) {
        return queryScoreService.queryScoreStatus(examId);
    }

    //向老师展示所有的班级
    @PostMapping("/class")
    public Result queryClassList(@RequestBody FindAllClassDTO findAllClassDTO) {
        return queryScoreService.queryClassList(findAllClassDTO);
    }

    @PostMapping("/examList")
    public Result queryExamList(@RequestBody FindExamListDTO findExamListDTO) {
        return queryScoreService.queryExamList(findExamListDTO);
    }

    @PostMapping("/studentList")
    public Result queryScoreStuList(@RequestBody FindExamStudentListDTO findExamStudentListDTO) {
        return queryScoreService.queryScoreList(findExamStudentListDTO);
    }

}
