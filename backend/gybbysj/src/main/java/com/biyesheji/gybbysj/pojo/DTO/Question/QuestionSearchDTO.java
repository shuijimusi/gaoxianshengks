package com.biyesheji.gybbysj.pojo.DTO.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class QuestionSearchDTO {
    private String content;
    private String chapter;
    private Integer pageNum;
    private Integer pageSize;
}
