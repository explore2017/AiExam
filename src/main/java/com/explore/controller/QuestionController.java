package com.explore.controller;

import com.explore.common.ServerResponse;
import com.explore.pojo.Question;
import com.explore.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    IQuestionService questionService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse save(Question question){
        return questionService.saveQuestion(question);
    }

}
