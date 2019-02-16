package com.explore.dao;

import com.explore.pojo.QuestionPaper;
import com.explore.pojo.QuestionPaperKey;

public interface QuestionPaperMapper {
    int deleteByPrimaryKey(QuestionPaperKey key);

    int insert(QuestionPaper record);

    int insertSelective(QuestionPaper record);

    QuestionPaper selectByPrimaryKey(QuestionPaperKey key);

    int updateByPrimaryKeySelective(QuestionPaper record);

    int updateByPrimaryKey(QuestionPaper record);
}