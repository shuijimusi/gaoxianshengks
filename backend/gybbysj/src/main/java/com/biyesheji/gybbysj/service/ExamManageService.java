package com.biyesheji.gybbysj.service;

import com.biyesheji.gybbysj.pojo.DTO.ExamManage.*;
import com.biyesheji.gybbysj.pojo.Result;

public interface ExamManageService {
    Result deleteExam(Long paperId);

    Result getExamList(GetPageListDTO getPageListDTO);

    Result getExamDetail(GetExamDetailDTO getExamDetailDTO);

    Result startExam(startExamDTO startExamDTO);

    Result getAllExamList(GetAllExamDTO getAllExamDTO);
}
