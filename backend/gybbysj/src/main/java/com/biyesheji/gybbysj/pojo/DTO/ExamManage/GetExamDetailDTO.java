package com.biyesheji.gybbysj.pojo.DTO.ExamManage;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetExamDetailDTO {
    private Long examid;
    private Long userid;
    private Integer paperId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
