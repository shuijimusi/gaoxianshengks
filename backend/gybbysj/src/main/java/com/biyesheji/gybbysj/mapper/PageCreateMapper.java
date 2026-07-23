package com.biyesheji.gybbysj.mapper;

import com.biyesheji.gybbysj.pojo.DTO.PageCreate.AddQuestionDTO;
import com.biyesheji.gybbysj.pojo.DTO.PageCreate.PageCreateDTO;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface PageCreateMapper {

    List<AddQuestionDTO> FindQuestionByCondition(AddQuestionDTO addQuestionDTO, Long userid);


    int createPaper(PageCreateDTO pageCreateDTO);


    int createPaperItem(PageCreateDTO pageCreateDTO);
}
