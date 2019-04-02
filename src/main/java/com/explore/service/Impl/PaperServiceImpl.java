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
import org.apache.commons.lang3.StringUtils;

import java.util.*;

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
        paper.setScore(0.0);
        if(paperMapper.insertSelective(paper)==1){
            return ServerResponse.createBySuccessMessage("添加试卷成功,请继续添加题目");
        }
       return ServerResponse.createByErrorMessage("添加试卷失败 ");
    }

    @Override
    public ServerResponse editPaperByPaperId(Integer paperId, Paper newPaper) {
        Paper paper=paperMapper.selectByPrimaryKey(paperId);
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        Date date = new Date();
        newPaper.setUpdateTime(date);
        int count =paperMapper.updateByPrimaryKeySelective(newPaper);
        if(count==0){return ServerResponse.createByErrorMessage("修改试卷失败");}
        return ServerResponse.createBySuccessMessage("修改成功");
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
    public ServerResponse getDetailsByPaperId(Integer paperId) {
        Paper paper=paperMapper.selectByPrimaryKey(paperId);
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        List<PaperCompose> paperComposes=paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperId);
        List<Question> questionList=new ArrayList<Question>();
        for(PaperCompose paperCompose:paperComposes){
            Question question=questionMapper.selectQuestionByQuestionId(paperCompose.getQuestionId());
            if(question!=null){
                if(false) {
                    question.setAnswer(StringUtils.EMPTY);
                }
                questionList.add(question);
            }
        }
        Map data=new HashMap();
        data.put("question",questionList);
        data.put("paperCompose",paperComposes);
       return ServerResponse.createBySuccess(data);
    }

    @Override
    public ServerResponse addPaperComposeByPaperId( PaperCompose paperCompose) {
        Paper paper=paperMapper.selectByPrimaryKey(paperCompose.getPaperId());
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        if(paperComposeMapper.selectPaperComposeByPaperIdAndQuestionId(paperCompose.getPaperId(),paperCompose.getQuestionId())!=null){
            return ServerResponse.createByErrorMessage("试卷中已有试题");
        }
        Integer sequence =paperComposeMapper.maxSequence(paperCompose.getPaperId());
        if(sequence==null){
            paperCompose.setSequence(0);
        }else {
            paperCompose.setSequence(sequence+1);
        }
        if(paperComposeMapper.insert(paperCompose)==0){
            return ServerResponse.createByErrorMessage("添加试题失败");
        }
        updatePaperScore(paperCompose.getPaperId());

        return ServerResponse.createBySuccessMessage("添加试题成功");
    }

    @Override
    public ServerResponse editPaperComposeByPaperId(Integer paperId, PaperCompose paperCompose) {
        Paper paper=paperMapper.selectByPrimaryKey(paperId);
        if(paper==null){return ServerResponse.createByErrorMessage("出错了");}
        PaperCompose oldPaperCompose=paperComposeMapper.selectPaperComposeByPaperIdAndQuestionId(paperCompose.getPaperId(),paperCompose.getQuestionId());
        if(oldPaperCompose==null) { return ServerResponse.createByErrorMessage("出错了");}
        paperCompose.setId(oldPaperCompose.getId());
        if(null==paperCompose.getSingleScore()){paperCompose.setSingleScore(0.0);}
        if( paperComposeMapper.updateByPrimaryKeySelective(paperCompose)==0){return ServerResponse.createByErrorMessage("出错了");}
        updatePaperScore(paperId);
        return  ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse deletePaperComposeBySequenceAndPaperId(Integer paperId, Integer sequence)  {
        Paper paper=paperMapper.selectByPrimaryKey(paperId);
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        PaperCompose paperCompose=paperComposeMapper.selectPaperComposeByPaperIdAndSequence(paperId,sequence);
        if(paperCompose==null){return ServerResponse.createByErrorMessage("删除失败");}
        int count= paperComposeMapper.updateTosequenceByPaperId(paperId,sequence);
        if(count==0){return ServerResponse.createByErrorMessage("删除失败");}
        paperComposeMapper.deleteByPrimaryKey(paperCompose.getId());
        updatePaperScore(paperId);
        return ServerResponse.createBySuccessMessage("删除成功");
    }

    @Override
    public ServerResponse autoQuestion(Integer paperId, Integer questionTypeId, Integer quantity, Double singeScore, Integer subjectId, String keyPoint) {
        List<Question> questionList=questionMapper.selectQuestionsByQuestionTypeIdAndSubjectId(questionTypeId,subjectId);
        if(questionList.size()!=quantity){return  ServerResponse.createByErrorMessage("找不到足够数量的题目");}
        int sequence=0;
        List<PaperCompose> paperComposeList=paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperId);
        if(paperComposeList!=null){sequence=paperComposeList.get(paperComposeList.size()-1).getSequence()+1;} //获得自动添加的序号
        for(int i=quantity;i>0;i--){
           if(questionList.size()==0){return  ServerResponse.createByErrorMessage("找不到足够数量的题目");}
            int rand=(int)(Math.random()*(questionList.size()-1));
            PaperCompose paperCompose=new PaperCompose();
            Question question=questionList.get(rand);
            if(paperComposeMapper.selectPaperComposeByPaperIdAndQuestionId(paperId,question.getId())!=null){
                questionList.remove(rand);
                i++;
                continue;
            }
            paperCompose.setPaperId(paperId);
            paperCompose.setQuestionId(question.getId());
            paperCompose.setQuestionTypeId(question.getQuestionTypeId());
            paperCompose.setSequence(sequence++);
            paperCompose.setSingleScore(singeScore);
            ServerResponse addServerResponse= this.addPaperComposeByPaperId(paperCompose); //考题的添加
            if(!addServerResponse.isSuccess()) {return  addServerResponse;}
            questionList.remove(rand);
        }
        return ServerResponse.createBySuccessMessage("生成成功");
    }

    @Override
    public ServerResponse checkPaper(Integer paperId, Double totalScore) {
        Paper paper=paperMapper.selectByPrimaryKey(paperId);
        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
        Double paperScore=0.0;
        List<PaperCompose> paperComposeList= paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperId);
        for(PaperCompose paperCompose:paperComposeList){
         paperScore+=paperCompose.getSingleScore();
       }
       if(paperScore.equals(totalScore)){
           paperMapper.updatePaperStatus(paperId,1);
           return  ServerResponse.createBySuccessMessage("试卷合格");
       }
        paperMapper.updatePaperStatus(paperId,0);
        return ServerResponse.createByErrorMessage("试卷不合格");
    }

    @Override
    public ServerResponse changeSequence(PaperCompose paperCompose) {

        if(paperCompose.getQuestionNum()==0){
            if(paperCompose.getSequence()==0){return  ServerResponse.createBySuccess();}
            PaperCompose paperCompose1= paperComposeMapper.selectPaperComposeByPaperIdAndQuestionId(paperCompose.getPaperId(),paperCompose.getQuestionId());
            if(paperCompose1==null){return ServerResponse.createByErrorMessage("发生了未知错误"); }
            List<PaperCompose> paperComposeList=paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperCompose.getPaperId());
            PaperCompose paperCompose2= paperComposeList.get(paperCompose.getSequence()-1);
            paperCompose2.setSequence(paperCompose.getSequence());
            paperComposeMapper.updateByPrimaryKeySelective(paperCompose2);
            paperCompose2=paperComposeList.get(paperCompose.getSequence());
            paperCompose2.setSequence(paperCompose.getSequence()-1);
            paperComposeMapper.updateByPrimaryKeySelective(paperCompose2);
            return  ServerResponse.createBySuccess();

        }else if(paperCompose.getQuestionNum()==1){
            Integer max=paperComposeMapper.maxSequence(paperCompose.getPaperId());
            if(max==paperCompose.getSequence()){return  ServerResponse.createBySuccess();}
            PaperCompose paperCompose1= paperComposeMapper.selectPaperComposeByPaperIdAndQuestionId(paperCompose.getPaperId(),paperCompose.getQuestionId());
            if(paperCompose1==null){return ServerResponse.createByErrorMessage("发生了未知错误"); }
            List<PaperCompose> paperComposeList=paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperCompose.getPaperId());
            PaperCompose paperCompose2= paperComposeList.get(paperCompose.getSequence()+1);
            paperCompose2.setSequence(paperCompose.getSequence());
            paperComposeMapper.updateByPrimaryKeySelective(paperCompose2);
            paperCompose2=paperComposeList.get(paperCompose.getSequence());
            paperCompose2.setSequence(paperCompose.getSequence()+1);
            paperComposeMapper.updateByPrimaryKeySelective(paperCompose2);
            return  ServerResponse.createBySuccess();

        }
            return ServerResponse.createByError();
    }

    @Override
    public ServerResponse updatePaperScore(Integer paperId) {
       List<PaperCompose> paperComposeList=paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperId);
        Double score=0.0;
       for(PaperCompose paperCompose:paperComposeList){
         if(null!=paperCompose.getSingleScore()){
             score+=paperCompose.getSingleScore();
         }
       }
       Paper paper=new Paper();
       paper.setId(paperId);
       paper.setScore(score);
       paperMapper.updateByPrimaryKeySelective(paper);
       return ServerResponse.createBySuccess(score);
    }


}
