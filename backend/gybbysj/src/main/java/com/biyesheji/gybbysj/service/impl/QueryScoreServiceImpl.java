package com.biyesheji.gybbysj.service.impl;

import com.biyesheji.gybbysj.mapper.QueryScoreMapper;
import com.biyesheji.gybbysj.pojo.DTO.PageCreate.AddQuestionDTO;
import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindAllClassDTO;
import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindExamListDTO;
import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindExamStudentListDTO;
import com.biyesheji.gybbysj.pojo.PageResult;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.QueryScoreService;
import com.biyesheji.gybbysj.util.UserContext;
import com.biyesheji.gybbysj.vo.QueryScore.queryExamListVO;
import com.biyesheji.gybbysj.vo.QueryScore.queryStuListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryScoreServiceImpl implements QueryScoreService {
    @Autowired
    private QueryScoreMapper queryScoreMapper;

    //查询学生考试状态
    @Override
    public Result queryScoreStatus(Long examId) {
        Long userid = UserContext.getUser().getUserId();
        Integer status = queryScoreMapper.queryScoreStatus(userid, examId);
        return Result.success(status);
    }

    //查询所有班级
    @Override
    public Result queryClassList(FindAllClassDTO findAllClassDTO) {
        findAllClassDTO.setUserId(UserContext.getUser().getUserId());
        Integer pageNum = findAllClassDTO.getPageNum();
        Integer pageSize = findAllClassDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<String> classList = queryScoreMapper.queryClassList(findAllClassDTO);
        return getResult(classList);
    }


    @Override
    public Result queryScoreList(FindExamStudentListDTO findExamStudentListDTO) {;
        Integer pageNum = findExamStudentListDTO.getPageNum();
        Integer pageSize = findExamStudentListDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<queryStuListVO> stuList = queryScoreMapper.queryStuList(findExamStudentListDTO);
        PageInfo<queryStuListVO> pageInfo = new PageInfo<>(stuList);
        PageResult<queryStuListVO> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRows(pageInfo.getList());
        return Result.success(pageResult);
    }

    @Override
    public Result queryExamList(FindExamListDTO findExamListDTO) {
        findExamListDTO.setUserId(UserContext.getUser().getUserId());
        Integer pageNum = findExamListDTO.getPageNum();
        Integer pageSize = findExamListDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<queryExamListVO> examList = queryScoreMapper.queryExamList(findExamListDTO);
        PageInfo<queryExamListVO> pageInfo = new PageInfo<>(examList);
        PageResult<queryExamListVO> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRows(pageInfo.getList());
        return Result.success(pageResult);
    }

    static Result getResult(List<String> classList) {
        PageInfo<String> pageInfo = new PageInfo<>(classList);
        PageResult<String> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRows(pageInfo.getList());
        return Result.success(pageResult);
    }
}
