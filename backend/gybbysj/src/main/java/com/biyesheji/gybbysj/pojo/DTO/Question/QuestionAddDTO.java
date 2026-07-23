package com.biyesheji.gybbysj.pojo.DTO.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionAddDTO {
    private Long id;
    private Integer type;
    private String chapter;
    private Integer difficulty;
    private String content;
    private String options;
    private String answer;
    private String analysis;
    private BigDecimal score;
    private String tags;
    private Long userId;
}
