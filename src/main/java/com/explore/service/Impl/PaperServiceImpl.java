package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.*;
import com.explore.pojo.*;
import com.explore.pojo.Class;
import com.explore.service.IPaperService;
import com.explore.vo.PaperComposeVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
    @Autowired
    ClassMapper classMapper;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    SubjectMapper subjectMapper;

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
        if(paper.getStatus()!=null&&paper.getStatus()==2){
                  return randomPaper(paper);
        }else {
            List<PaperComposeVo> paperComposeVos=paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperId);
            for(PaperComposeVo paperComposeVo:paperComposeVos){
                Question question=questionMapper.selectQuestionByQuestionId(paperComposeVo.getQuestionId());
                if(question!=null){
                    question.setAnswer(StringUtils.EMPTY);
                    paperComposeVo.setQuestion(question);
                }
            }
            return ServerResponse.createBySuccess(paperComposeVos);
        }

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
//        List<Question> questionList=questionMapper.selectQuestionsByQuestionTypeIdAndSubjectId(questionTypeId,subjectId);
//        if(questionList.size()!=quantity){return  ServerResponse.createByErrorMessage("找不到足够数量的题目");}
//        int sequence=0;
//        List<PaperCompose> paperComposeList=paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperId);
//        if(paperComposeList!=null){sequence=paperComposeList.get(paperComposeList.size()-1).getSequence()+1;} //获得自动添加的序号
//        for(int i=quantity;i>0;i--){
//           if(questionList.size()==0){return  ServerResponse.createByErrorMessage("找不到足够数量的题目");}
//            int rand=(int)(Math.random()*(questionList.size()-1));
//            PaperCompose paperCompose=new PaperCompose();
//            Question question=questionList.get(rand);
//            if(paperComposeMapper.selectPaperComposeByPaperIdAndQuestionId(paperId,question.getId())!=null){
//                questionList.remove(rand);
//                i++;
//                continue;
//            }
//            paperCompose.setPaperId(paperId);
//            paperCompose.setQuestionId(question.getId());
//            paperCompose.setQuestionTypeId(question.getQuestionTypeId());
//            paperCompose.setSequence(sequence++);
//            paperCompose.setSingleScore(singeScore);
//            ServerResponse addServerResponse= this.addPaperComposeByPaperId(paperCompose); //考题的添加
//            if(!addServerResponse.isSuccess()) {return  addServerResponse;}
//            questionList.remove(rand);
//        }
        return ServerResponse.createBySuccessMessage("生成成功");
    }

    @Override
    public ServerResponse checkPaper(Integer paperId, Double totalScore) {
//        Paper paper=paperMapper.selectByPrimaryKey(paperId);
//        if(paper==null){return ServerResponse.createByErrorMessage("找不到该试卷");}
//        Double paperScore=0.0;
//        List<PaperCompose> paperComposeList= paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperId);
//        for(PaperCompose paperCompose:paperComposeList){
//         paperScore+=paperCompose.getSingleScore();
//       }
//       if(paperScore.equals(totalScore)){
//           paperMapper.updatePaperStatus(paperId,1);
//           return  ServerResponse.createBySuccessMessage("试卷合格");
//       }
//        paperMapper.updatePaperStatus(paperId,0);
//        return ServerResponse.createByErrorMessage("试卷不合格");
        return null;
    }

    @Override
    public ServerResponse changeSequence(PaperCompose paperCompose) {

        if(paperCompose.getQuestionNum()==0){
            if(paperCompose.getSequence()==0){return  ServerResponse.createBySuccess();}
            PaperCompose paperCompose1= paperComposeMapper.selectPaperComposeByPaperIdAndQuestionId(paperCompose.getPaperId(),paperCompose.getQuestionId());
            if(paperCompose1==null){return ServerResponse.createByErrorMessage("发生了未知错误"); }
            List<PaperComposeVo> paperComposeList=paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperCompose.getPaperId());
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
            List<PaperComposeVo> paperComposeList=paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperCompose.getPaperId());
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
       List<PaperComposeVo> paperComposeList=paperComposeMapper.selectQuestionByPaperIdOrderBySequence(paperId);
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

    @Override
    public ServerResponse getPaperByClass(Integer classId) {
        Class class1=classMapper.selectByPrimaryKey(classId);
        if(class1==null){
            return  ServerResponse.createByErrorMessage("找不到该班级");
        }
        Teacher teacher=teacherMapper.selectByPrimaryKey(class1.getTeacherId());
        if(teacher==null){
            return  ServerResponse.createByErrorMessage("出错了");
        }
        if(subjectMapper.selectByPrimaryKey(class1.getSubjectId())==null){
            return  ServerResponse.createByErrorMessage("找不到班级科目");
        }
        List<Paper> paperList=  paperMapper.selectPaperBySubject(class1.getId(),teacher.getUsername());
        return ServerResponse.createBySuccess(paperList);
    }

    @Override
    public ServerResponse addRandomPaper(Paper paper) {
        Class class1=classMapper.selectByPrimaryKey(paper.getSubjectId());
        if(class1==null){
            return  ServerResponse.createByErrorMessage("找不到该班级");
        }
        Subject subject=subjectMapper.selectByPrimaryKey(class1.getSubjectId());
        if(subject==null){
            return  ServerResponse.createByErrorMessage("找不到班级科目");
        }
        paper.setSubjectId(subject.getId());
        paper.setCreateTime(new Date());
        paper.setStatus(2);
        paper.setUsufruct(-1);
        paperMapper.insert(paper);
        return ServerResponse.createBySuccessMessage("设计试卷成功",paper);
    }

    public ServerResponse randomPaper(Paper paper){
        Integer simple=0;
        Integer middle=0;
        Integer hard=0;
        List<Question> questionList=new ArrayList<Question>();
        List<String> number=new ArrayList<String>();        //题目数量key集合
        List<String> keyPoint=new ArrayList<String>();        //知识点key集合
        List<Double> questionScore=new ArrayList<Double>();       //题目分数集合
        JSONObject jsonObject =  JSONObject.fromObject(paper.getDescribe());
        try{
            Iterator iterator = jsonObject.keys();
            while(iterator.hasNext()){
                String  key1 = (String) iterator.next();
                number.add(key1);
                String  key2 = (String) iterator.next();
                keyPoint.add(key2);
                String  key3 = (String) iterator.next();
                Double score=jsonObject.getDouble(key3);
                questionScore.add(score);
            }
        }catch (Exception e){
            ServerResponse.createByErrorMessage("发生了未知错误");
        }

        List<Question> allQuestions=  questionMapper.selectQuestionsByCondition(paper.getSubjectId(),null,null,null);
        for(int i=0;i<6;i++){
            final Integer targetNumber=jsonObject.getInt(number.get(i));
            if(targetNumber==null||targetNumber==0){continue;}
            Integer  questionNumber=0;                                       //记录已添加的题目数量
            String[] keyPoints=jsonObject.getString(keyPoint.get(i)).split(",");
            if(paper.getDifficulty()==1){             //记录难度
                simple=(int)(0.4*targetNumber);
                middle=(int)(0.4*targetNumber);
                hard=(int)(0.2*targetNumber);
            }else if(paper.getDifficulty()==2){
                simple=(int)(0.3*targetNumber);
                middle=(int)(0.4*targetNumber);
                hard=(int)(0.3*targetNumber);
            } else{
                simple=(int)(0.6*targetNumber);
                middle=(int)(0.3*targetNumber);
                hard=(int)(0.1*targetNumber);
            }
                    List<Question> commonQuestions=new ArrayList<>();          //分出普通的问题集合和知识点相关的集合
                    List<Question> keyPointQuestions=new ArrayList<>();
                    boolean questionStatus=false;
                    for(Question question:allQuestions){
                        if(question.getQuestionTypeId()==i){
                            if(question.getKeyPoint()==null||question.getKeyPoint().equals("")){
                                commonQuestions.add(question);
                                continue;
                            }
                            for(int j=0;j<keyPoints.length;j++){
                                if(keyPoints[j].contains(question.getKeyPoint())){
                                    keyPointQuestions.add(question);
                                    questionStatus=true;
                                    break;
                                }
                            }
                            if(!questionStatus){
                                commonQuestions.add(question);
                            }
                        }
                    }
                    int time=0;
                    int limit=(int)(1.5*keyPointQuestions.size());
              while(questionNumber<targetNumber){         //从知识点集合随机取题
                  if(time>limit&&(targetNumber-questionNumber)<commonQuestions.size()){break;}
                  Random random=new Random();
                  if(keyPointQuestions.size()==0){break;}
                  if(keyPointQuestions.size()==1){
                      Question question=  commonQuestions.get(0);
                      questionList.add(question);
                      commonQuestions.remove(0);
                      questionNumber++;
                      break;
                  }
                  int index=random.nextInt(keyPointQuestions.size()-1);
                  Question question=  keyPointQuestions.get(index);
                  if(time>(2*limit)){
                      questionList.add(question);
                      keyPointQuestions.remove(index);
                      questionNumber++;
                      continue;
                  }
                  if(simple>0){
                      if(question.getDifficulty()==0){
                          questionList.add(question);
                          keyPointQuestions.remove(index);
                          questionNumber++;
                          simple--;
                          continue;
                      }
                  }
                  if(middle>0){
                      if(question.getDifficulty()==1){
                          questionList.add(question);
                          keyPointQuestions.remove(index);
                          questionNumber++;
                          middle--;
                          continue;
                      }
                  }
                  if(hard>0){
                      if(question.getDifficulty()==2){
                          questionList.add(question);
                          keyPointQuestions.remove(index);
                          questionNumber++;
                          hard--;
                          continue;
                      }
                  }
                time++;
              }
              time=0;
              limit=commonQuestions.size();                        //从普通问题集合随机取题
              while (questionNumber<targetNumber){
                  Random random=new Random();
                  if(commonQuestions.size()==0){break;}
                  if(commonQuestions.size()==1){
                      Question question=  commonQuestions.get(0);
                      questionList.add(question);
                      commonQuestions.remove(0);
                      questionNumber++;
                      break;
                  }
                  int index=random.nextInt(commonQuestions.size()-1);
                  Question question=  commonQuestions.get(index);
                  if(question==null){
                      break;
                  }
                  if(time>limit||simple<1&&middle<1&&hard<1){
                      questionList.add(question);
                      commonQuestions.remove(index);
                      questionNumber++;
                      continue;
                  }
                  if(simple>0){
                      if(question.getDifficulty()==0){
                          questionList.add(question);
                          commonQuestions.remove(index);
                          questionNumber++;
                          simple--;
                          continue;
                      }
                  }
                  if(middle>0){
                      if(question.getDifficulty()==1){
                          questionList.add(question);
                          commonQuestions.remove(index);
                          questionNumber++;
                          middle--;
                          continue;
                      }
                  }
                  if(hard>0){
                      if(question.getDifficulty()==2){
                          questionList.add(question);
                          commonQuestions.remove(index);
                          questionNumber++;
                          hard--;
                          continue;
                      }
                  }
                  time++;
              }
        }
        Double realScore=0.0;
        List<PaperComposeVo> paperComposeVos=new ArrayList<PaperComposeVo>();
        for(int i=0;i<questionList.size();i++){
            Question question=  questionList.get(i);
            PaperComposeVo paperComposeVo=new PaperComposeVo();
            paperComposeVo.setQuestionId(question.getId());
            paperComposeVo.setSequence(i);
            Double score=questionScore.get(question.getQuestionTypeId());
            paperComposeVo.setSingleScore(score);
            realScore+=score;
            question.setAnswer(StringUtils.EMPTY);
            paperComposeVo.setQuestion(question);
            paperComposeVos.add(paperComposeVo);
        }
        if(!realScore.equals(paper.getTotalScore())){
            PaperComposeVo paperComposeVo=paperComposeVos.get(paperComposeVos.size()-1);
            paperComposeVo.setSingleScore(paper.getTotalScore()-realScore);
        }
        return ServerResponse.createBySuccess(paperComposeVos);
    }

}
