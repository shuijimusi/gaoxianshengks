package com.biyesheji.gybbysj.vo.ExamProcess;

import com.biyesheji.gybbysj.pojo.Entity.ExamStudentAnswer;
import com.biyesheji.gybbysj.pojo.Entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamProcessReturnVO {
    List<Question> questions;
    List<ExamStudentAnswer> examStudentAnswers;
    private Long examId;
}
