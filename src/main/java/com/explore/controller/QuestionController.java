package com.explore.controller;

import com.explore.common.ServerResponse;
import com.explore.pojo.Question;
import com.explore.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    IQuestionService questionService;
    @PostMapping
    public ServerResponse save(Question question){
        return questionService.saveQuestion(question);
    }
    @PutMapping(value ="/{id}")
    public  ServerResponse edit(@PathVariable("id")Integer questionId,Question question){return  questionService.editQuestionByQuestionId(questionId,question);}
    @DeleteMapping(value ="/{id}")
    public ServerResponse delect(@PathVariable("id")Integer questionId){return  questionService.delectQuestionByQuestionId(questionId);}
    @GetMapping(value ="/{id}")
    public  ServerResponse<Question> get(@PathVariable("id")Integer questionId){return  questionService.getQuestionsByQuestionId(questionId);}
    @GetMapping
    public  ServerResponse<List<Question>> get(){return  questionService.getAllQuestions();}




}
