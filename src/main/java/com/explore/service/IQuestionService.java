package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Question;

import java.util.List;

public interface IQuestionService {
    ServerResponse saveQuestion(Question question);
    ServerResponse editQuestionByQuestionId(Question newQuestion);
    ServerResponse delectQuestionByQuestionId(Integer questionId);
    ServerResponse<Question> getQuestionsByQuestionId(Integer questionId);
    ServerResponse<List<Question>> getAllQuestions();
}
