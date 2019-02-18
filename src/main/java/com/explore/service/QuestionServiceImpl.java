package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.dao.QuestionMapper;
import com.explore.pojo.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Override
    public ServerResponse saveQuestion(Question question) {
        Date date = new Date();
        question.setCreateTime(date);
        question.setUpdateTime(date);
        questionMapper.insert(question);
        return ServerResponse.createBySuccessMessage("添加成功");
    }

    @Override
    public List<Question> getQuestionsByPaperId(Integer paper_id) {
        return questionMapper.selectQuestionsByPaperId(paper_id);
    }
}
