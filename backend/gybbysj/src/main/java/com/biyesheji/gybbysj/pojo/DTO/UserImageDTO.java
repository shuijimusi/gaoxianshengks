package com.biyesheji.gybbysj.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserImageDTO {
    private Long userId;
    private String username;
    private Integer role;
}
