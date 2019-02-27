package com.explore.pojo;

public class PaperCompose {
    private Integer id;

    private Integer paperId;

    private Integer questionTypeId;

    private Integer questionId;

    private Integer questionNum;

    private Integer sequence;

    private Double singleScore;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getQuestionTypeId() {
        return questionTypeId;
    }
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionTypeId(Integer questionTypeId) {
        this.questionTypeId = questionTypeId;
    }
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Double getSingleScore() {
        return singleScore;
    }

    public void setSingleScore(Double singleScore) {
        this.singleScore = singleScore;
    }
}