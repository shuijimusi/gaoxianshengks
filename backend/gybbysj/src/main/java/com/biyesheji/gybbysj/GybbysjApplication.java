package com.biyesheji.gybbysj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.biyesheji.gybbysj.mapper")
public class GybbysjApplication {

    public static void main(String[] args) {
        SpringApplication.run(GybbysjApplication.class, args);
    }

}
