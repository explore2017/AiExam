package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Question;

import java.util.List;

public interface IQuestionService {
    ServerResponse saveQuestion(Question question);

    List<Question> getQuestionsByPaperId(Integer paper_id);
}
