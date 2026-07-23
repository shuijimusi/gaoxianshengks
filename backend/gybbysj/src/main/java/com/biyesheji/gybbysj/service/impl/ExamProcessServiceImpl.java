package com.biyesheji.gybbysj.service.impl;

import com.biyesheji.gybbysj.mapper.ExamProcessMapper;
import com.biyesheji.gybbysj.pojo.DTO.ExamManage.GetExamDetailDTO;
import com.biyesheji.gybbysj.pojo.Entity.ExamStudent;
import com.biyesheji.gybbysj.pojo.Entity.ExamStudentAnswer;
import com.biyesheji.gybbysj.pojo.Entity.Question;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.ExamProcessService;
import com.biyesheji.gybbysj.util.UserContext;
import com.biyesheji.gybbysj.vo.ExamProcess.ExamProcessReturnVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExamProcessServiceImpl implements ExamProcessService {
    @Autowired
    private ExamProcessMapper examProcessMapper;

    @Override
    public Result searchExamQuestion(Long examId) {
        Integer paperId = examProcessMapper.FindPaperIdByExamId(examId);
        if (paperId == null){
            return Result.error("该考试不存在");
        }
        //先拿到考试详情，判断考试是否结束
        GetExamDetailDTO getExamDetailDTO = examProcessMapper.FindExamDetailByExamId(examId);
        if (getExamDetailDTO == null){
            return Result.error("该考试不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        // ① 当前时间 > end\_time → 【考试已经结束，禁止进入】
        if (now.isAfter(getExamDetailDTO.getEndTime())) {
            throw new RuntimeException("本场考试已结束，无法进入");
        }
        // 可选增强：当前时间 < start\_time → 考试还未开始，暂不能进入
        if (now.isBefore(getExamDetailDTO.getStartTime())) {
            throw new RuntimeException("考试尚未开始，请等待开考");
        }
        //看学生是否已经参加过考试
        Long userid = UserContext.getUser().getUserId();
        ExamStudent examStudent = examProcessMapper.FindExamDetailByUserIdAndExamId(userid,examId);
        if (examStudent != null && examStudent.getStuStatus() == 2) {
            return Result.error("该考试已完成，无法重复参加");
        }
        if (examStudent == null) {
            //没参加创建考试学生，状态设置为1，开始时间设置为当前时间
            ExamStudent examStudent1 = new ExamStudent();
            examStudent1.setExamCourseId(examId);
            examStudent1.setStudentId(userid);
            examStudent1.setStuStatus(1);
            examStudent1.setStartTime(now);
            examProcessMapper.insert(examStudent1);
            examStudent = examStudent1;
        }


        // 根据paperId查询所有题目

        List<Question> questionList = examProcessMapper.FindQuestionByPaperId(paperId);

        //查看学生有没有答题记录
        //根据 `exam_student.id` 查询副表 `exam_student_answer`
        ExamProcessReturnVO examProcessReturnVO = new ExamProcessReturnVO();
        examProcessReturnVO.setQuestions(questionList);
        List<ExamStudentAnswer> examStudentAnswerList = examProcessMapper.FindExamStudentAnswerByExamStudent(examStudent,examId);
        examProcessReturnVO.setExamStudentAnswers(examStudentAnswerList);
        examProcessReturnVO.setExamId(examId);
        return Result.success(examProcessReturnVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cacheQuestion(Map<String, Object> data) {
        Integer i = 0;
        Long examId = null;//考试的id
        Long examStudentId = null;//考试学生综合id
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String keyStr = entry.getKey();
            // 需要转数字就自己转
            if(i == 0){
                Object value = entry.getValue();
                log.info("考试id:" + value + "现在开始提交");
                examId = Long.parseLong(value.toString());
                examStudentId = examProcessMapper.FindExamDetailByUserIdAndExamId(UserContext.getUser().getUserId(),examId).getId();
                i++;
            }
            else {
                Long keyId = Long.parseLong(keyStr.substring(2));
                Object value = entry.getValue();
                Integer MCQ = examProcessMapper.FindQuestionTypeById(keyId);
                ExamStudentAnswer examStudentAnswer = new ExamStudentAnswer();
                if(MCQ == 1 || MCQ == 2 || MCQ == 3 || MCQ == 4){
                    // 单选题直接得到分数
                        BigDecimal score = examProcessMapper.FindQuestionIsRight(keyId,value.toString(),examId);
                        if (score != null && score.compareTo(BigDecimal.ZERO) != 0){
                            log.info("题目:" + keyId + ",答案:" + value + "现在已经开始缓存，分数为:" + score);
                            // 正确
                            examStudentAnswer.setQuestionScore(score);
                            examStudentAnswer.setIsCorrect(1);
                        }
                        else{
                            // 错误
                            examStudentAnswer.setQuestionScore(BigDecimal.ZERO);
                            examStudentAnswer.setIsCorrect(0);
                        }
                }
                if(MCQ == 5 || MCQ == 6){
                    examStudentAnswer.setQuestionScore(BigDecimal.ZERO);
                }
                log.info("题目:" + keyId + ",答案:" + value + "现在已经开始缓存");
                examStudentAnswer.setExamStudentId(examStudentId);
                examStudentAnswer.setQuestionId(keyId);
                examStudentAnswer.setStudentAnswer(value.toString());
                //有则更新，无则插入
                Long isExist =examProcessMapper.searchQuestionIsExist(examStudentAnswer);
                try {
                    examProcessMapper.insertQuestionSubmit(examStudentAnswer);
                } catch (DuplicateKeyException e) {
                    log.warn("记录已存在，执行更新 examStudentId:{},questionId:{}",
                            examStudentAnswer.getExamStudentId(), examStudentAnswer.getQuestionId());
                    examStudentAnswer.setId(isExist);
                    examProcessMapper.updateQuestionSubmit(examStudentAnswer);
                }
            }
        }
        examProcessMapper.changeSubmitTime(examId);
        return Result.success("缓存成功");
    }

    @Override
    public Result submitExam(Map<String, Object> data) {
        Integer i = 0;
        Long examId = null;
        Long examStudentId = null;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String keyStr = entry.getKey();
            // 需要转数字就自己转
            if(i == 0){
                Object value = entry.getValue();
                log.info("考试id:" + value + "现在开始提交");
                examId = Long.parseLong(value.toString());
                examStudentId = examProcessMapper.FindExamDetailByUserIdAndExamId(UserContext.getUser().getUserId(),examId).getId();
                i++;
            }
            else {
                Long keyId = Long.parseLong(keyStr.substring(2));
                Object value = entry.getValue();
                log.info("题目:" + keyId + ",答案:" + value + "现在已经开始提交");
                ExamStudentAnswer examStudentAnswer = new ExamStudentAnswer();
                examStudentAnswer.setExamStudentId(examStudentId);
                examStudentAnswer.setQuestionId(keyId);
                examStudentAnswer.setStudentAnswer(value.toString());
                //有则更新，无则插入
                Long isExist =examProcessMapper.searchQuestionIsExist(examStudentAnswer);
                try {
                    examProcessMapper.insertQuestionSubmit(examStudentAnswer);
                } catch (DuplicateKeyException e) {
                    log.warn("考试记录已存在，执行更新 examStudentId:{},questionId:{}",
                            examStudentAnswer.getExamStudentId(), examStudentAnswer.getQuestionId());
                    examStudentAnswer.setId(isExist);
                    examProcessMapper.updateQuestionSubmit(examStudentAnswer);
                }
            }
        }
        examProcessMapper.endExam(examId);
        return Result.success("提交成功");
    }
}
