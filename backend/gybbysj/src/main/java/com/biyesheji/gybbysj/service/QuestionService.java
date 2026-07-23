package com.biyesheji.gybbysj.service;

import com.biyesheji.gybbysj.pojo.DTO.Question.QuestionAddDTO;
import com.biyesheji.gybbysj.pojo.DTO.Question.QuestionSearchDTO;
import com.biyesheji.gybbysj.pojo.Result;

public interface QuestionService {
    Result returnAllQuestion();

    Result addQuestion(QuestionAddDTO questionAddDTO);

    Result returnAllQuestionByCondition(QuestionSearchDTO questionSearchDTO);

    Result deleteQuestion(Long questionId);

    Result updateQuestion(QuestionAddDTO questionAddDTO);

    Result getOneImage(Long questionId);
}
