package com.explore.controller;

import com.explore.common.ServerResponse;
import com.explore.pojo.Question;
import com.explore.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/question")
public class QuestionController {

    @Autowired
    IQuestionService questionService;
    /**
     * 添加问题
     */
    @PostMapping
    public ServerResponse save(Question question){
        return questionService.saveQuestion(question);
    }
    /**
     * 修改问题
     */
    @PutMapping(value ="/{id}")
    public  ServerResponse edit(@PathVariable("id")Integer questionId,Question question){return  questionService.editQuestionByQuestionId(questionId,question);}
    /**
     * 删除试题
     */
    @DeleteMapping(value ="/{id}")
    public ServerResponse delete(@PathVariable("id")Integer questionId){return  questionService.deleteQuestionByQuestionId(questionId);}
    /**
     * 获得指定问题
     */
    @GetMapping(value ="/{id}")
    public  ServerResponse<Question> get(@PathVariable("id")Integer questionId){return  questionService.getQuestionsByQuestionId(questionId);}
    /**
     * 获得所有问题
     */
    @GetMapping
    public  ServerResponse<List<Question>> get(){return  questionService.getAllQuestions();}




}
