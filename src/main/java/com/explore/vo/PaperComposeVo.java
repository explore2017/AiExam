package com.explore.vo;

import com.explore.pojo.PaperCompose;
import com.explore.pojo.Question;

public class PaperComposeVo extends PaperCompose {
    private Question question;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}
