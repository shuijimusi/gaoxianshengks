package com.biyesheji.gybbysj.service;

import com.biyesheji.gybbysj.pojo.Result;

import java.util.Map;

public interface ExamProcessService {
    Result searchExamQuestion(Long examId);

    Result cacheQuestion(Map<String, Object> data);

    Result submitExam(Map<String, Object> data);
}
