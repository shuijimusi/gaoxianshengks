package com.biyesheji.gybbysj.vo.Question;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuestionSearchVO {
    private Long id;
    private Integer type;
    private String chapter;
    private String content;
    private Integer difficulty;
    private BigDecimal score;
}
