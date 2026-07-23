package com.biyesheji.gybbysj.vo.Question;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 题目表实体
 */
@Data
public class OneQuestionImageVO {

    private Long id;

    /**
     * 题型(1-单选, 2-多选, 3-判断, 4-填空, 5-简答, 6-编程)
     */
    private Integer type;

    /**
     * 所属章节
     */
    private String chapter;

    /**
     * 难度(1-简单, 2-中等, 3-困难)
     */
    private Integer difficulty;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 选项(JSON格式)
     */
    private String options;

    /**
     * 答案
     */
    private String answer;

    /**
     * 答案解析
     */
    private String analysis;

    /**
     * 分值
     */
    private BigDecimal score;

    /**
     * 标签(逗号分隔)
     */
    private String tags;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 状态(0-禁用, 1-启用)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}