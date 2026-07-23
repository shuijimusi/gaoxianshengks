package com.biyesheji.gybbysj.service.impl;


import com.biyesheji.gybbysj.mapper.QuestionMapper;
import com.biyesheji.gybbysj.pojo.DTO.Question.QuestionAddDTO;
import com.biyesheji.gybbysj.pojo.DTO.Question.QuestionSearchDTO;
import com.biyesheji.gybbysj.pojo.Entity.Question;
import com.biyesheji.gybbysj.pojo.PageResult;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.QuestionService;
import com.biyesheji.gybbysj.util.UserContext;
import com.biyesheji.gybbysj.vo.Question.QuestionSearchVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    // 查询所有问题
    @Override
    public Result returnAllQuestion() {
        Integer count = questionMapper.returnAllQuestion(UserContext.getUser().getUserId());
        return Result.success(count);
    }

    // 添加问题
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addQuestion(QuestionAddDTO questionAddDTO) {
        Long userId = UserContext.getUserId();
        questionAddDTO.setUserId(userId);
        return questionMapper.addQuestion(questionAddDTO)==1?Result.success("添加成功"):Result.error("添加失败");
    }

    @Override
    public Result returnAllQuestionByCondition(QuestionSearchDTO questionSearchDTO) {
        Integer pageNum = questionSearchDTO.getPageNum();
        Integer pageSize = questionSearchDTO.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        List<QuestionSearchVO> questionList = questionMapper.FindQuestionByCondition(questionSearchDTO.getChapter(),questionSearchDTO.getContent(),UserContext.getUser().getUserId());
        PageInfo<QuestionSearchVO> pageInfo = new PageInfo<>(questionList);

        // 4. 封装成 PageResult 返回
        PageResult<QuestionSearchVO> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRows(pageInfo.getList());
        return Result.success(pageResult);
    }

    @Override
    public Result deleteQuestion(Long questionId) {
        return questionMapper.deleteQuestion(questionId)==1?Result.success("删除成功"):Result.error("删除失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateQuestion(QuestionAddDTO questionAddDTO) {
            Long userId = UserContext.getUserId();
            questionAddDTO.setUserId(userId);
            return questionMapper.updateQuestion(questionAddDTO)==1?Result.success("更新成功"):Result.error("更新失败");
    }

    @Override
    public Result getOneImage(Long questionId) {
        return Result.success(questionMapper.getOneImage(questionId));
    }


}
