package com.biyesheji.gybbysj.pojo.DTO.PageCreate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionScoreDTO {
    private Long questionId;
    private Integer questionType;
    private Integer questionScore;
}
