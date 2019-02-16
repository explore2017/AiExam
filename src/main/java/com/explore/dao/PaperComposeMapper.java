package com.explore.dao;

import com.explore.pojo.PaperCompose;

public interface PaperComposeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PaperCompose record);

    int insertSelective(PaperCompose record);

    PaperCompose selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaperCompose record);

    int updateByPrimaryKey(PaperCompose record);
}