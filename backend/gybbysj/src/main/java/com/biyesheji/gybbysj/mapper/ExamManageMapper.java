package com.biyesheji.gybbysj.mapper;

import com.biyesheji.gybbysj.pojo.DTO.ExamManage.*;
import com.biyesheji.gybbysj.vo.ExamManage.GetAllExamVO;
import com.biyesheji.gybbysj.vo.ExamManage.GetExamDetailVO;
import com.biyesheji.gybbysj.vo.ExamManage.GetExamListVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ExamManageMapper {

    List<GetExamListVO> getExamList(GetPageListDTO getExamListDTO);

    @Update("update exam_paper set deleted=1 where id=#{paperId} and creator_id = #{userId}")
    void deleteExam(Long paperId, Long userId);

    GetExamDetailVO getExamDetail(GetExamDetailDTO getExamDetailDTO);

    int updateEndTimeAndStatus(startExamDTO startExamDTO);

    void updateExamTimeout(@Param("now") LocalDateTime now);

    void updateExamTimefront(@Param("now") LocalDateTime now);

    List<GetAllExamVO> getAllExam(GetAllExamDTO getAllExamDTO);
}
