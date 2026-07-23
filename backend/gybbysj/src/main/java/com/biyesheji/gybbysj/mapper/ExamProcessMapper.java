package com.biyesheji.gybbysj.mapper;

import com.biyesheji.gybbysj.pojo.DTO.ExamManage.GetExamDetailDTO;
import com.biyesheji.gybbysj.pojo.Entity.ExamStudent;
import com.biyesheji.gybbysj.pojo.Entity.ExamStudentAnswer;
import com.biyesheji.gybbysj.pojo.Entity.Question;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ExamProcessMapper {
    @Select("select paper_id from exam_course where id = #{examId}")
    Integer FindPaperIdByExamId(Long examId);

    List<Question> FindQuestionByPaperId(Integer paperId);

    @Select("select paper_id,start_time,end_time from exam_course where id = #{examId}")
    GetExamDetailDTO FindExamDetailByExamId(Long examId);

    @Select("select * from exam_student where student_id = #{userid} and exam_course_id = #{examId}")
    ExamStudent FindExamDetailByUserIdAndExamId(Long userid, Long examId);

    @Insert("insert into exam_student (exam_course_id,student_id,stu_status,start_time) values (#{examCourseId},#{studentId},#{stuStatus},#{startTime})")
    void insert(ExamStudent examStudent1);

    @Select("select * from exam_student_answer esa " +
            "inner join exam_student es on esa.exam_student_id = es.id " +
            " where es.student_id = #{student.studentId}" +
            " and es.exam_course_id = #{eId}")
    List<ExamStudentAnswer> FindExamStudentAnswerByExamStudent(@Param("student") ExamStudent examStudent,
                                                               @Param("eId") Long examId);

        @Select("select id from exam_student_answer where exam_student_id = #{examStudentId} and question_id = #{questionId}")
    Long searchQuestionIsExist(ExamStudentAnswer examStudentAnswer);

//    @Update("update exam_student_answer set student_draft = #{studentDraft} where id = #{id}")
//    void updateQuestion(ExamStudentAnswer examStudentAnswer);
//
//    @Insert("insert into exam_student_answer (exam_student_id,question_id,student_draft) values (#{examStudentId},#{questionId},#{studentDraft})")
//    void insertQuestion(ExamStudentAnswer examStudentAnswer);

    @Update("update exam_student_answer set student_answer = #{studentAnswer},question_score = #{questionScore},is_correct=#{isCorrect},update_time=now() where id = #{id}")
    void updateQuestionSubmit(ExamStudentAnswer examStudentAnswer);

    @Insert("insert into exam_student_answer (exam_student_id,question_id,student_answer,question_score,is_correct,update_time) values (#{examStudentId},#{questionId},#{studentAnswer},#{questionScore},#{isCorrect},now())")
    void insertQuestionSubmit(ExamStudentAnswer examStudentAnswer);

    @Update("update exam_student set stu_status = 2,submit_time = now() where exam_course_id = #{examId}")
    void endExam(Long examId);

    @Select("select type from question where id = #{keyId}")
    Integer FindQuestionTypeById(Long keyId);


    BigDecimal FindQuestionIsRight(Long keyId, String stuAnswer,Long examId);

    @Update("update exam_student set submit_time = now() where exam_course_id = #{examId}")
    void changeSubmitTime(Long examId);
}
