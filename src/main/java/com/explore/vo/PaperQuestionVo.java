package com.explore.vo;

import lombok.Data;

/**
 * 学生端试卷题目
 * @author PinTeh
 * @date 2019/4/24
 */
@Data
public class PaperQuestionVo {
    private Integer sequence;

    private Double singleScore;

    private QuestionVo question;

    private String reply;
}
