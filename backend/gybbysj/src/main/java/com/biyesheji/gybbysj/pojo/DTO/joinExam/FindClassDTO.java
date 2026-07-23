package com.biyesheji.gybbysj.pojo.DTO.joinExam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindClassDTO {
    private Long userid;
    private String classname;
    private Integer pageNum;
    private Integer pageSize;
}
