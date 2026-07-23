package com.biyesheji.gybbysj.service.impl;

import com.biyesheji.gybbysj.mapper.ExamManageMapper;
import com.biyesheji.gybbysj.mapper.JoinExamMapper;
import com.biyesheji.gybbysj.pojo.DTO.joinExam.FindAllExamDTO;
import com.biyesheji.gybbysj.pojo.DTO.joinExam.FindClassDTO;
import com.biyesheji.gybbysj.pojo.PageResult;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.JoinExamService;
import com.biyesheji.gybbysj.util.UserContext;
import com.biyesheji.gybbysj.vo.JoinExam.FindAllExamVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.biyesheji.gybbysj.service.impl.QueryScoreServiceImpl.getResult;

@Slf4j
@Service
public class JoinExamServiceImpl implements JoinExamService {
    @Autowired
    private JoinExamMapper joinExamMapper;
    @Autowired
    private ExamManageMapper examManMapper;
    @Override
    public Result findAllExam(FindAllExamDTO findAllExamDTO) {
        log.info("需要看的内容在这里！！！！！");

        LocalDateTime now = LocalDateTime.now();
        examManMapper.updateExamTimeout(now);
        examManMapper.updateExamTimefront(now);


        Long userid = UserContext.getUser().getUserId();
        findAllExamDTO.setUserid(userid);
        Integer pageNum = findAllExamDTO.getPageNum();
        Integer pageSize = findAllExamDTO.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        List<FindAllExamVO> questionList = joinExamMapper.findAllExam(findAllExamDTO);
        PageInfo<FindAllExamVO> pageInfo = new PageInfo<>(questionList);

        // 4. 封装成 PageResult 返回
        PageResult<FindAllExamVO> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRows(pageInfo.getList());
        log.info("需要看的内容在结束啦！！！！！");
        return Result.success(pageResult);
    }


    @Override
    public Result findClass(FindClassDTO findClassDTO) {
        Long userid = UserContext.getUser().getUserId();
        findClassDTO.setUserid(userid);
        Integer pageNum = findClassDTO.getPageNum();
        Integer pageSize = findClassDTO.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        List<String> questionList = joinExamMapper.findClass(findClassDTO);
        return getResult(questionList);
    }
}
