package com.biyesheji.gybbysj.pojo.DTO.QueryScore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindExamListDTO {
    private Long userId;
    private String courseName;
    private Integer pageNum;
    private Integer pageSize;
}
