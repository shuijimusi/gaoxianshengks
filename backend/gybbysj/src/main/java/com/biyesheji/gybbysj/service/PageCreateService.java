package com.biyesheji.gybbysj.service;

import com.biyesheji.gybbysj.pojo.DTO.PageCreate.AddQuestionDTO;
import com.biyesheji.gybbysj.pojo.DTO.PageCreate.PageCreateDTO;
import com.biyesheji.gybbysj.pojo.Result;

public interface PageCreateService {
    Result selectQuestion(AddQuestionDTO addQuestionDTO);

    Result createPaper(PageCreateDTO pageCreateDTO);
}
