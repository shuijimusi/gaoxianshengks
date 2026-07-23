package com.biyesheji.gybbysj.pojo.DTO.ExamManage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPageListDTO {
    private Integer pageNum;
    private Integer pageSize;
    private Long userid;
}
