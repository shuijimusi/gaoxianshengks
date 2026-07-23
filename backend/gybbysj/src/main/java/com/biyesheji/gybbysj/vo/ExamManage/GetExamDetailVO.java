package com.biyesheji.gybbysj.vo.ExamManage;

import com.ethlo.time.DateTime;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetExamDetailVO {
    private String title;
    private String description;
    private BigDecimal totalScore;
    private Integer duration;
    private BigDecimal passScore;
    private Integer questionCount;
    private String createTime;
    private String updateTime;
    private Long creatorId;
    private Long id;
}

