package com.explore.controller;

import com.explore.common.ServerResponse;
import com.explore.pojo.Paper;
import com.explore.pojo.PaperCompose;
import com.explore.pojo.Question;
import com.explore.service.IPaperService;
import com.explore.service.IQuestionService;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(value = "试卷库的接口")
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
    @ApiOperation(value="向试卷库新增试卷基本信息", notes="参数为一个Paper对象")
    public ServerResponse save(@RequestBody Paper paper){
        return paperService.savePaper(paper);
    }
    /**
     * 根据试卷id 获取试卷基础信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value="获得指定试卷基本信息", notes="通过id获得")
    public ServerResponse<Paper> getPaperById(@PathVariable("id")Integer id){
        return paperService.getPaperById(id);
    }

    /**
     * 获取所有试卷基础信息
     * @return
     */
    @GetMapping
    @ApiOperation(value="获得所有试卷基本信息", notes="不需要参数")
    public ServerResponse<List<Paper>> getAllPaper(){
        ServerResponse serverResponse=paperService.getAllPaper();
        return serverResponse;
    }

    /**
     * 删除试卷
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value="删除试卷", notes="通过id删除")
    public ServerResponse deletePaperByPaperId(@PathVariable("id")Integer id){
        return paperService.deletePaperByPaperId(id);
    }

    /**
     * 修改试卷基本信息
     * @param id,paper
     */
    @PutMapping(value ="/{id}")
    @ApiOperation(value="修改试卷基本信息", notes="通过id和一个Paper对象")
    public ServerResponse edit(@PathVariable("id")Integer id,Paper paper){
        return paperService.editPaperByPaperId(id,paper);
    }

    /**
     * 根据试卷id 获取题目
     * @param paperId
     * @return
     */
    @GetMapping(value = "/details/{paperId}")
    @ApiOperation(value="获得指定试卷的题目信息", notes="通过指定试卷的id")
    public ServerResponse<List<Question>> get(@PathVariable("paperId")Integer paperId){
        return paperService.getDetailsByPaperId(paperId);
    }

    /**
     * 向试卷添加题目
     * @param paperId,paperCompose
     */
    @PostMapping(value = "/details/{paperId}")
    @ApiOperation(value="向指定试卷添加题目", notes="指定试卷的id和一个PaperCompose对象")
    public ServerResponse addPaperComposeByPaperId(@RequestBody @PathVariable("paperId")Integer paperId, PaperCompose paperCompose){
        return paperService.addPaperComposeByPaperId(paperId,paperCompose);
    }

    /**
     * 修改试卷题目
     * @param paperId,paperCompose
     */
    @PutMapping(value = "/details/{paperId}")
    @ApiOperation(value="向指定试卷修改题目", notes="指定试卷的id和一个PaperCompose对象")
        public ServerResponse editPaperComposeByPaperId(@PathVariable("paperId")Integer paperId, PaperCompose paperCompose){
        return paperService.editPaperComposeByPaperId(paperId,paperCompose);
    }
    /**
     * 删除试卷题目
     * @param paperId,paperCompose
     */
    @DeleteMapping(value = "/details/{paperId}")
    @ApiOperation(value="向指定试卷输出指定题目", notes="指定试卷的id和题目的序列号")
    public ServerResponse deletePaperComposeBySequenceAndPaperId(@PathVariable("paperId")Integer paperId, Integer sequence){
        return paperService.deletePaperComposeBySequenceAndPaperId(paperId,sequence);
    }

    /**
     * 自动生成试卷题目
     * @param paperId,questionTypeId,quantity,singeScore,subjectId,keyPoint
     */
    @PostMapping(value = "/auto/{paperId}")
    @ApiOperation(value="自动生成试卷的题目", notes="通过试卷id，问题类型id，生成的数量，每一个题目的成绩，科目id，知识点")
    public ServerResponse<List<Question>> autoQuestion(@RequestBody @PathVariable("paperId")Integer paperId,  Integer questionTypeId, Integer quantity, Double singeScore, Integer subjectId, String keyPoint){
        return paperService.autoQuestion(paperId,questionTypeId,quantity,singeScore,subjectId,keyPoint);
    }

}
