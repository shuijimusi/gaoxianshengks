package com.biyesheji.gybbysj.pojo.Entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学生每题答题明细表
 */
@Data
@TableName("exam_student_answer")
public class ExamStudentAnswer {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联exam_student主表id
     */
    private Long examStudentId;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 学生正式答案（提交试卷后赋值）
     */
    private String studentAnswer;

    /**
     * 草稿内容，前端定时保存，提交前持续更新
     */
    private String studentDraft;

    /**
     * 本题得分
     */
    private BigDecimal questionScore;

    /**
     * 是否答对：0 错误，1 正确；主观题等待批改可存null
     */
    private Integer isCorrect;

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
