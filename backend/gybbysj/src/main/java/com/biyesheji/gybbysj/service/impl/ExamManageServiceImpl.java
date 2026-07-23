package com.biyesheji.gybbysj.service.impl;


import com.biyesheji.gybbysj.mapper.ExamManageMapper;
import com.biyesheji.gybbysj.pojo.DTO.ExamManage.*;
import com.biyesheji.gybbysj.pojo.PageResult;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.ExamManageService;
import com.biyesheji.gybbysj.util.UserContext;
import com.biyesheji.gybbysj.vo.ExamManage.GetAllExamVO;
import com.biyesheji.gybbysj.vo.ExamManage.GetExamListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamManageServiceImpl implements ExamManageService {

    @Autowired
    private ExamManageMapper examManMapper;

    @Override
    public Result deleteExam(Long paperId) {
        examManMapper.deleteExam(paperId, UserContext.getUser().getUserId());
        return Result.success("删除成功");
    }

    @Override

    public Result getExamList(GetPageListDTO getExamListDTO) {
        Integer pageNum = getExamListDTO.getPageNum();
        Integer pageSize = getExamListDTO.getPageSize();
        getExamListDTO.setUserid(UserContext.getUser().getUserId());

        // 更新考试状态模块
        LocalDateTime now = LocalDateTime.now();
        examManMapper.updateExamTimefront(now);
        examManMapper.updateExamTimeout(now);

        //
        PageHelper.startPage(pageNum,pageSize);
        List<GetExamListVO> examList = examManMapper.getExamList(getExamListDTO);
        PageInfo<GetExamListVO> pageInfo = new PageInfo<>(examList);

        // 4. 封装成 PageResult 返回
        PageResult<GetExamListVO> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRows(pageInfo.getList());
        return Result.success(pageResult);
    }

    @Override
    public Result getExamDetail(GetExamDetailDTO getExamDetailDTO) {
        getExamDetailDTO.setUserid(UserContext.getUser().getUserId());

        // 更新考试状态模块
        LocalDateTime now = LocalDateTime.now();
        examManMapper.updateExamTimeout(now);
        examManMapper.updateExamTimefront(now);

        //
        return Result.success(examManMapper.getExamDetail(getExamDetailDTO));
    }

    @Override
    public Result startExam(startExamDTO startExamDTO) {
        startExamDTO.setUserId(UserContext.getUser().getUserId());
        if(startExamDTO.getStartTime() == null) {
            startExamDTO.setStartTime(LocalDateTime.now());
        }
        LocalDateTime now = startExamDTO.getStartTime();
        Integer duration = startExamDTO.getDuration();
        LocalDateTime endTime = now.plusMinutes(duration);
        startExamDTO.setEndTime(endTime);
        int affectRow = examManMapper.updateEndTimeAndStatus(startExamDTO);
        if (affectRow <= 0) {
            return Result.error("开启失败！仅未开始的试卷且仅创建者可开启");
        }
        return Result.success("开始考试成功");
    }


    // 所有考试
    @Override
    public Result getAllExamList(GetAllExamDTO getAllExamDTO) {
        Integer pageNum = getAllExamDTO.getPageNum();
        Integer pageSize = getAllExamDTO.getPageSize();
        getAllExamDTO.setUserid(UserContext.getUser().getUserId());

        // 更新考试状态模块
        LocalDateTime now = LocalDateTime.now();
        examManMapper.updateExamTimefront(now);
        examManMapper.updateExamTimeout(now);

        //
        PageHelper.startPage(pageNum,pageSize);
        List<GetAllExamVO> examList = examManMapper.getAllExam(getAllExamDTO);
        PageInfo<GetAllExamVO> pageInfo = new PageInfo<>(examList);

        // 4. 封装成 PageResult 返回
        PageResult<GetAllExamVO> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRows(pageInfo.getList());
        return Result.success(pageResult);
    }
}
