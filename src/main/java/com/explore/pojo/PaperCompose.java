package com.explore.pojo;

public class PaperCompose {
    private Integer id;

    private String name;

    private Integer isSubjective;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getIsSubjective() {
        return isSubjective;
    }

    public void setIsSubjective(Integer isSubjective) {
        this.isSubjective = isSubjective;
    }
}