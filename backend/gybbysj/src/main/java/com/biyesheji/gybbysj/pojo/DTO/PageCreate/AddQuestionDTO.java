package com.biyesheji.gybbysj.pojo.DTO.PageCreate;

import lombok.Data;

@Data
public class AddQuestionDTO {
    private String chapter;
    private Integer difficulty;
    private Integer pageNum;
    private Integer pageSize;
    private Integer type;
}
