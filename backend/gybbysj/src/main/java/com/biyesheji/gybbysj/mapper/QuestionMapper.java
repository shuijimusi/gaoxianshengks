package com.biyesheji.gybbysj.mapper;

import com.biyesheji.gybbysj.pojo.DTO.Question.QuestionAddDTO;
import com.biyesheji.gybbysj.pojo.Entity.Question;
import com.biyesheji.gybbysj.vo.Question.OneQuestionImageVO;
import com.biyesheji.gybbysj.vo.Question.QuestionSearchVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface QuestionMapper {
    @Select("select count(*) from question where creator_id = #{userId}")
    Integer returnAllQuestion(Long userId);

    @Insert("insert into question(type,chapter,difficulty,content,options,answer,analysis,score,tags,creator_id,status) values(#{type},#{chapter},#{difficulty},#{content},#{options},#{answer},#{analysis},#{score},#{tags},#{userId}, 0)")
    int addQuestion(QuestionAddDTO questionAddDTO);

    List<QuestionSearchVO> FindQuestionByCondition(String chapter, String content, Long userId);

    @Update("update question set status = 1 where id = #{questionId}")
    int deleteQuestion(Long questionId);

    @Update("update question set type = #{type},chapter = #{chapter},difficulty = #{difficulty},content = #{content},options = #{options},answer = #{answer},analysis = #{analysis},score = #{score},tags = #{tags} where id = #{id}")
    int updateQuestion(QuestionAddDTO questionAddDTO);

    @Select("select * from question where id = #{questionId}")
    OneQuestionImageVO getOneImage(Long questionId);
}
