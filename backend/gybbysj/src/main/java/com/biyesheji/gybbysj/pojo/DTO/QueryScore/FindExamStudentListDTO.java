package com.biyesheji.gybbysj.pojo.DTO.QueryScore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindExamStudentListDTO {
    private Long examId;
    private Integer pageNum;
    private Integer pageSize;
}
