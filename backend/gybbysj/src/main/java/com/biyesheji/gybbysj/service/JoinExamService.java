package com.biyesheji.gybbysj.service;

import com.biyesheji.gybbysj.pojo.DTO.joinExam.FindAllExamDTO;
import com.biyesheji.gybbysj.pojo.DTO.joinExam.FindClassDTO;
import com.biyesheji.gybbysj.pojo.Result;

public interface JoinExamService {
    Result findAllExam(FindAllExamDTO findAllExamDTO);

    Result findClass(FindClassDTO findClassDTO);
}
