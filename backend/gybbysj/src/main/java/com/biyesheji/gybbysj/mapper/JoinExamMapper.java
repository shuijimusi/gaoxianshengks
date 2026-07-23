package com.biyesheji.gybbysj.mapper;

import com.biyesheji.gybbysj.pojo.DTO.joinExam.FindAllExamDTO;
import com.biyesheji.gybbysj.pojo.DTO.joinExam.FindClassDTO;
import com.biyesheji.gybbysj.vo.JoinExam.FindAllExamVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JoinExamMapper {

    List<FindAllExamVO> findAllExam(FindAllExamDTO findAllExamDTO);

    List<String> findClass(FindClassDTO findClassDTO);
}
