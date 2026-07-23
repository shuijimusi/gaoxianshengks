package com.biyesheji.gybbysj.vo.ExamManage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllExamVO {
    private String title;
    private String username;
    private String courseName;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
