package com.explore.controller;

import com.explore.common.ServerResponse;
import com.explore.pojo.Paper;
import com.explore.pojo.PaperCompose;
import com.explore.pojo.Question;
import com.explore.service.IPaperService;
import com.explore.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/paper")
public class PaperController {

    @Autowired
    IPaperService paperService;

    @Autowired
    IQuestionService questionService;

    /**
     * 新增试卷基本信息
     * @param paper
     * @return
     */
    @PostMapping
    public ServerResponse save(Paper paper){
        return paperService.savePaper(paper);
    }
    /**
     * 根据试卷id 获取试卷基础信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ServerResponse<Paper> getPaperById(@PathVariable("id")Integer id){
        return paperService.getPaperById(id);
    }

    /**
     * 获取所有试卷基础信息
     * @return
     */
    @GetMapping
    public ServerResponse<List<Paper>> getAllPaper(){
        return paperService.getAllPaper();
    }

    /**
     * 删除试卷
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    public ServerResponse deletePaperByPaperId(@PathVariable("id")Integer id){
        return paperService.deletePaperByPaperId(id);
    }

    /**
     * 修改试卷基本信息
     * @param id,paper
     */
    @PutMapping(value ="/{id}")
    public ServerResponse edit(@PathVariable("id")Integer id,Paper paper){
        return paperService.editPaperByPaperId(id,paper);
    }

    /**
     * 根据试卷id 获取题目
     * @param paperId
     * @return
     */
    @GetMapping(value = "/details/{paperId}")
    public ServerResponse<List<Question>> get(@PathVariable("paperId")Integer paperId){
        return paperService.getDetailsByPaperId(paperId);
    }

    /**
     * 向试卷添加题目
     * @param paperId,paperCompose
     */
    @PostMapping(value = "/details/{paperId}")
    public ServerResponse addPaperComposeByPaperId(@PathVariable("paperId")Integer paperId, PaperCompose paperCompose){
        return paperService.addPaperComposeByPaperId(paperId,paperCompose);
    }

    /**
     * 修改试卷题目
     * @param paperId,paperCompose
     */
    @PutMapping(value = "/details/{paperId}")
    public ServerResponse editPaperComposeByPaperId(@PathVariable("paperId")Integer paperId, PaperCompose paperCompose){
        return paperService.editPaperComposeByPaperId(paperId,paperCompose);
    }
    /**
     * 删除试卷题目
     * @param paperId,paperCompose
     */
    @DeleteMapping(value = "/details/{paperId}")
    public ServerResponse deletePaperComposeBySequenceAndPaperId(@PathVariable("paperId")Integer paperId, Integer sequence){
        return paperService.deletePaperComposeBySequenceAndPaperId(paperId,sequence);
    }

    /**
     * 自动生成试卷题目
     * @param paperId,questionTypeId,quantity,singeScore,subjectId,keyPoint
     */
    @PostMapping(value = "/auto/{paperId}")
    public ServerResponse<List<Question>> autoQuestion(@PathVariable("paperId")Integer paperId,  Integer questionTypeId, Integer quantity, Double singeScore, Integer subjectId, String keyPoint){
        return paperService.autoQuestion(paperId,questionTypeId,quantity,singeScore,subjectId,keyPoint);
    }

}
