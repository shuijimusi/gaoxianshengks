package com.biyesheji.gybbysj.mapper;

import com.biyesheji.gybbysj.pojo.DTO.PageCreate.AddQuestionDTO;
import com.biyesheji.gybbysj.pojo.Entity.Course;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CreateClassMapper {
    @Insert("insert into course(course_name,course_code,teacher_id,status,student_num) values(#{courseName},#{uuid},#{userId},1,0)")
    int createClass(String courseName, String uuid, Long userId);

    @Select("select * from course where teacher_id = #{userId} and status = 1")
    List<Course> getCourseByTeacherId(Long userId);

    @Update("update course set status = 0 where id = #{courseId}")
    int deleteClass(Long courseId);

    @Select("select id from course where course_code = #{courseCode}")
    String getCourseByCourseCode(String courseCode);

    @Insert("insert into student_course(course_id,student_id) values(#{courseId},#{userId})")
    int joinClass(String courseId, Long userId);



    @Update("update course set student_num = student_num + 1 where id = #{courseId}")
    void addclassnum(String courseId);


    List<Course> getCourse(Long userId);
}
