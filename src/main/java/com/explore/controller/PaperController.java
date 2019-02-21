package com.explore.controller;

import com.explore.common.ServerResponse;
import com.explore.pojo.Paper;
import com.explore.pojo.Question;
import com.explore.service.IPaperService;
import com.explore.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/paper")
public class PaperController {

    @Autowired
    IPaperService paperService;

    @Autowired
    IQuestionService questionService;

    /**
     * 新增试卷
     * @param paper
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse save(Paper paper){
        return paperService.savePaper(paper);
    }

    /**
     * 根据试卷id 获取题目
     * @param paper_id
     * @return
     */
    @RequestMapping(value = "/detail/{paper_id}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Question>> detail(@PathVariable("paper_id")Integer paper_id){
       return null;
    }

}
