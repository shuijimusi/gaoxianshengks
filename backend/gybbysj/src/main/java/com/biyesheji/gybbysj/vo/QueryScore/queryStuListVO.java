package com.biyesheji.gybbysj.vo.QueryScore;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class queryStuListVO {
    private String username;
    private Integer stuStatus;
    private Long stuId;
    private LocalDateTime submitTime;
}
