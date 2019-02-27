package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.PaperComposeMapper;
import com.explore.dao.PaperMapper;
import com.explore.dao.QuestionMapper;
import com.explore.pojo.Paper;
import com.explore.pojo.PaperCompose;
import com.explore.pojo.Question;
import com.explore.service.IPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Transactional
@Service
public class PaperServiceImpl implements IPaperService {

    @Autowired
    PaperMapper paperMapper;
    @Autowired
    PaperComposeMapper paperComposeMapper;
    @Autowired
    QuestionMapper questionMapper;

    @Override
    public ServerResponse savePaper(Paper paper) {
        Date date = new Date();
        paper.setCreateTime(date);
        paper.setUpdateTime(date);
        paperMapper.insert(paper);
        return ServerResponse.createBySuccessMessage("添加成功 ");
    }

    @Override
    public ServerResponse editPaperByPaperId(Integer paperId, Paper newPaper) {
        Paper paper=paperMapper.selectByPrimaryKey(paperId);
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        Date date = new Date();
        newPaper.setUpdateTime(date);
        int count =paperMapper.updateByPrimaryKey(newPaper);
        if(count==0){return ServerResponse.createByErrorMessage("修改试卷失败");}
        return ServerResponse.createBySuccess("修改成功");
    }

    @Override
    public ServerResponse deletePaperByPaperId(Integer paperId) {
        Paper paper=paperMapper.selectByPrimaryKey(paperId);
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        int count= paperComposeMapper.deleteByPaperId(paperId);
        if(count==0){ServerResponse.createByErrorMessage("删除失败");}
        count=paperMapper.deleteByPrimaryKey(paperId);
        if(count==0){ServerResponse.createByErrorMessage("删除失败");}
        return ServerResponse.createBySuccessMessage("删除成功");
    }

    @Override
    public ServerResponse<Paper> getPaperById(Integer id) {
        Paper paper=paperMapper.selectByPrimaryKey(id);
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        return ServerResponse.createBySuccessMessage("试卷基本信息",paper);
    }

    @Override
    public ServerResponse<List<Paper>> getAllPaper() {
        return ServerResponse.createBySuccess(paperMapper.selectAllPaper());
    }

    @Override
    public ServerResponse<List<Question>> getDetailsByPaperId(Integer paperId) {
        Paper paper=paperMapper.selectByPrimaryKey(paperId);
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        List<PaperCompose> paperComposes=paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperId);
        List<Question> questionList=new ArrayList<Question>();
        for(PaperCompose paperCompose:paperComposes){
            questionList.add(questionMapper.selectQuestionByQuestionId(paperCompose.getQuestionId()));
        }
       return ServerResponse.createBySuccessMessage("返回试卷详情成功",questionList);
    }

    @Override
    public ServerResponse addPaperComposeByPaperId( Integer paperId,PaperCompose paperCompose) {
        Paper paper=paperMapper.selectByPrimaryKey(paperId);
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        paperCompose.setPaperId(paperId);
        int count =paperComposeMapper.insert(paperCompose);
        if(count==0){return ServerResponse.createByErrorMessage("添加试题失败");}
        return ServerResponse.createBySuccessMessage("添加试题成功");
    }

    @Override
    public ServerResponse editPaperComposeByPaperId(Integer paperId, PaperCompose paperCompose) {
        Paper paper=paperMapper.selectByPrimaryKey(paperId);
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        PaperCompose oldpaperCompose=paperComposeMapper.selectByPrimaryKey(paperCompose.getId());
        if(oldpaperCompose==null) { return ServerResponse.createByErrorMessage("修改试题失败");}
      int count= paperComposeMapper.updateByPrimaryKeySelective(paperCompose);
        if(count==0){return ServerResponse.createByErrorMessage("修改试题失败");}
        return  ServerResponse.createBySuccessMessage("修改试题成功");
    }

    @Override
    public ServerResponse deletePaperComposeBySequenceAndPaperId(Integer paperId, Integer sequence)  {
        Paper paper=paperMapper.selectByPrimaryKey(paperId);
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        PaperCompose paperCompose=paperComposeMapper.selectPaperComposeBySequenceAndPaperId(paperId,sequence);
        if(paperCompose==null){return ServerResponse.createByErrorMessage("删除失败");}
        int count= paperComposeMapper.updateTosequenceByPaperId(paperId,sequence);
        if(count==0){return ServerResponse.createByErrorMessage("删除失败");}
        count=paperComposeMapper.deleteByPrimaryKey(paperCompose.getId());
        if(count==0){return ServerResponse.createByErrorMessage("删除失败");}
        return ServerResponse.createBySuccessMessage("删除成功");
    }


}
