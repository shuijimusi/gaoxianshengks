package com.biyesheji.gybbysj.vo.ExamManage;

import com.ethlo.time.DateTime;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetExamListVO {
    private String title;
    private String description;
    private BigDecimal totalScore;
    private Integer duration;
    private Integer questionCount;
    private Long id;
}
