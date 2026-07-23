package com.biyesheji.gybbysj.service.impl;

import com.biyesheji.gybbysj.mapper.CreateClassMapper;
import com.biyesheji.gybbysj.mapper.PageCreateMapper;
import com.biyesheji.gybbysj.pojo.DTO.PageCreate.AddQuestionDTO;
import com.biyesheji.gybbysj.pojo.DTO.PageCreate.PageCreateDTO;
import com.biyesheji.gybbysj.pojo.PageResult;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.PageCreateService;
import com.biyesheji.gybbysj.util.UserContext;
import com.biyesheji.gybbysj.vo.Question.QuestionSearchVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Console;
import java.util.List;

@Service
@Slf4j
public class PageCreateServiceImpl implements PageCreateService {

    @Autowired
    private PageCreateMapper pageCreateMapper;

    @Override
    public Result selectQuestion(AddQuestionDTO addQuestionDTO) {
        Long userid = UserContext.getUser().getUserId();
        Integer pageNum = addQuestionDTO.getPageNum();
        Integer pageSize = addQuestionDTO.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        List<AddQuestionDTO> questionList = pageCreateMapper.FindQuestionByCondition(addQuestionDTO,userid);
        PageInfo<AddQuestionDTO> pageInfo = new PageInfo<>(questionList);

        // 4. 封装成 PageResult 返回
        PageResult<AddQuestionDTO> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRows(pageInfo.getList());
        return Result.success(pageResult);
    }

    // 创建试卷
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result createPaper(PageCreateDTO pageCreateDTO) {
        Long userid = UserContext.getUser().getUserId();
        pageCreateDTO.setUserId(userid);
        int i=pageCreateMapper.createPaper(pageCreateDTO);
        log.info("i的值为：{}",i);
        if(i !=1){
            throw new RuntimeException("创建失败");
        }
        int k=pageCreateMapper.createPaperItem(pageCreateDTO);
        log.info("k的值为：{}",k);
        if(k<1){
            throw new RuntimeException("创建失败");
        }
        return Result.success("创建成功");
    }
}
