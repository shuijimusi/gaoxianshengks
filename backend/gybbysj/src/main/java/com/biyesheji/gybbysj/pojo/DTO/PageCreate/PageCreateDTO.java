package com.biyesheji.gybbysj.pojo.DTO.PageCreate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageCreateDTO {
    private String title;
    private Long userId;
    private String description;
    private BigDecimal totalScore;
    private Integer duration;
    private BigDecimal passScore;
    private Integer questionCount;
    private List<QuestionScoreDTO> questionIdScore;
    private Long paperId;
}
