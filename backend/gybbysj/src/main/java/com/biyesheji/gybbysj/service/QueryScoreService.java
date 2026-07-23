package com.biyesheji.gybbysj.service;

import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindAllClassDTO;
import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindExamListDTO;
import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindExamStudentListDTO;
import com.biyesheji.gybbysj.pojo.Result;

public interface QueryScoreService {
    Result queryScoreStatus(Long examId);

    Result queryClassList(FindAllClassDTO findAllClassDTO);

    Result queryScoreList(FindExamStudentListDTO findExamStudentListDTO);

    Result queryExamList(FindExamListDTO findExamListDTO);
}
