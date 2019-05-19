package com.explore.vo;

import lombok.Data;

/**
 * @author PinTeh
 * @date 2019/4/24
 */
@Data
public class QuestionVo {
    private Integer id;

    private String title;

    private String content;

    private String selects;

    private Integer questionTypeId;

    private String answer;

    private  String img;
}
