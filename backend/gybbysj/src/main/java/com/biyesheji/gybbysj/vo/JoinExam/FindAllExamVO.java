package com.biyesheji.gybbysj.vo.JoinExam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllExamVO {
    private Long courseid;
    private Long id;
    private String title;
    private String courseName;
    private String username;
    private Integer duration;
    private Integer status;
    private LocalDateTime endTime;
    private LocalDateTime startTime;
}
