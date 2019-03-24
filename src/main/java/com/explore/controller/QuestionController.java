package com.explore.controller;

import com.explore.common.ServerResponse;
import com.explore.pojo.Question;
import com.explore.service.IQuestionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(value = "试题库的接口")
@RequestMapping(value = "/question")
public class QuestionController {

    @Autowired
    IQuestionService questionService;
    /**
     * 添加问题
     */
    @PostMapping
    @ApiOperation(value="向试题库添加试题", notes="参数为一个Question对象")
    public ServerResponse save(@RequestBody Question question){
        return questionService.saveQuestion(question);
    }
    /**
     * 修改问题
     */
    @PutMapping(value ="/{id}")
    @ApiOperation(value="向试题库修改试题", notes="通过id和一个Question对象进行修改")
    public  ServerResponse edit(@PathVariable("id")Integer questionId,Question question){return  questionService.editQuestionByQuestionId(questionId,question);}
    /**
     * 删除试题
     */
    @DeleteMapping(value ="/{id}")
    @ApiOperation(value="向试题库删除试题", notes="通过id删除试题")
    public ServerResponse delete(@PathVariable("id")Integer questionId){return  questionService.deleteQuestionByQuestionId(questionId);}
    /**
     * 获得指定问题
     */
    @GetMapping(value ="/{id}")
    @ApiOperation(value="从试题库获得指定试题", notes="通过id获得")
    public  ServerResponse<Question> get(@PathVariable("id")Integer questionId){return  questionService.getQuestionsByQuestionId(questionId);}
    /**
     * 获得所有问题
     */
    @GetMapping
    @ApiOperation(value="从试题库获得所有试题", notes="不需要参数")
    public  ServerResponse<List<Question>> get(){
        return questionService.getAllQuestions();
    }




}
