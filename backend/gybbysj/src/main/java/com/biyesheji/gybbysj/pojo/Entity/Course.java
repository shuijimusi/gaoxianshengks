package com.biyesheji.gybbysj.pojo.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {

    /**
     * 主键自增id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 老师id
     */
    private Long teacherId;

    /**
     * 课程编码
     */
    private String courseCode;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态
     */
    private Integer status;


    /**
     * 学生数量
     */
    private Integer studentNum;
}