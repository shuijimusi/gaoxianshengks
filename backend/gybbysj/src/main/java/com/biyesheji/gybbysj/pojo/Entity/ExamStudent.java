package com.biyesheji.gybbysj.pojo.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 学生考试主记录表
 */
@Data
@TableName("exam_student")
public class ExamStudent {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 考试场次id，对应 exam_course 主键
     */
    private Long examCourseId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 学生考试状态
     * 0 未参加（懒生成方案基本不用）
     * 1 正在参加考试
     * 2 已完成考试（提交试卷）
     */
    private Integer stuStatus;

    /**
     * 进入考试时间
     */
    private LocalDateTime startTime;

    /**
     * 试卷提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 本次考试最终得分
     */
    private Double totalScore;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
