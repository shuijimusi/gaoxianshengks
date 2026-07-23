package com.biyesheji.gybbysj.pojo.DTO.ExamManage;

import com.ethlo.time.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class startExamDTO {
    private Long paperId;
    private Long userId;
    private Long courseId;
    private Integer duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
