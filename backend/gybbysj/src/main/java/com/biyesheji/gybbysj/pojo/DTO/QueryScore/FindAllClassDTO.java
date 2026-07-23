package com.biyesheji.gybbysj.pojo.DTO.QueryScore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllClassDTO {
    private String classname;
    private Long userId;
    private Integer pageNum;
    private Integer pageSize;
}
