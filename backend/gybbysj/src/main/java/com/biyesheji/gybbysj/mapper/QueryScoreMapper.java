package com.biyesheji.gybbysj.mapper;

import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindAllClassDTO;
import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindExamListDTO;
import com.biyesheji.gybbysj.pojo.DTO.QueryScore.FindExamStudentListDTO;
import com.biyesheji.gybbysj.vo.QueryScore.queryExamListVO;
import com.biyesheji.gybbysj.vo.QueryScore.queryStuListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QueryScoreMapper {
    @Select("select stu_status from exam_student where student_id=#{userId} and exam_course_id=#{examId}")
    Integer queryScoreStatus(Long userId, Long examId);

    @Select("select course_name from course where course_name like #{classname}" +
            " and teacher_id=#{userId}")
    List<String> queryClassList(FindAllClassDTO findAllClassDTO);

    List<queryStuListVO> queryStuList(FindExamStudentListDTO findExamStudentListDTO);

    @Select("select id from exam_course where course_name=#{classname}")
    Long searchExamId(String classname);

    List<queryExamListVO> queryExamList(FindExamListDTO findExamListDTO);
}
