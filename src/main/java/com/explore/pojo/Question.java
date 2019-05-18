package com.explore.pojo;

import java.util.Date;

public class Question {
    private Integer id;

    private String title;

    private String content;

    private Integer questionTypeId;

    private String answer;

    private Date createTime;

    private Date updateTime;

    private Integer isSubjective;

    private Integer difficulty;

    private Integer subjectId;

    private Integer status;

    private String keyPoint;

    private String defaultScore;

    private String selects;

    private String img;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDefaultScore() {
        return defaultScore;
    }

    public void setSelects(String selects) {
        this.selects = selects == null ? null : selects.trim();
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public void setDefaultScore(String defaultScore) {
        this.defaultScore = defaultScore == null ? null : defaultScore.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(Integer questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public String getAnswer() {
        return answer;
    }

    public String getSelects() {
        return selects;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsSubjective() {
        return isSubjective;
    }

    public void setIsSubjective(Integer isSubjective) {
        this.isSubjective = isSubjective;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getKeyPoint() {
        return keyPoint;
    }

    public void setKeyPoint(String keyPoint) {
        this.keyPoint = keyPoint == null ? null : keyPoint.trim();
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}