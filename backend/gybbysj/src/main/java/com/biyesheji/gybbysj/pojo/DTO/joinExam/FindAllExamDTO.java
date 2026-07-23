package com.biyesheji.gybbysj.pojo.DTO.joinExam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllExamDTO {
    private Long courseId;
    private Long userid;
    private Integer pageNum;
    private Integer pageSize;
}
