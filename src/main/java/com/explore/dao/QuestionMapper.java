package com.explore.dao;

import com.explore.pojo.Question;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface QuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    Question selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Question record);

    List<Question> selectAllQuestions();

    Question selectQuestionByQuestionId(Integer questionId);

    List<Question> selectQuestionsByQuestionTypeIdAndSubjectId(@Param("questionTypeId") Integer questionTypeId, @Param("subjectId") Integer subjectId);
}