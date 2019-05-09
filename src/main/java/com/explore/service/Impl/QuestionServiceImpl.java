package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.QuestionMapper;
import com.explore.pojo.Question;
import com.explore.service.IQuestionService;
import com.explore.utils.ReadExcel;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Override
    public ServerResponse saveQuestion(Question question) {
        Date date = new Date();
        question.setCreateTime(date);
        question.setUpdateTime(date);
        questionMapper.insert(question);
        return ServerResponse.createBySuccessMessage("添加成功");
    }

    @Override
    public ServerResponse editQuestionByQuestionId(Integer questionId,Question newQuestion) {
      Question question= questionMapper.selectQuestionByQuestionId(questionId);
      if(question==null){return ServerResponse.createByErrorMessage("找不到这个题目");}
      Date date = new Date();
      newQuestion.setUpdateTime(date);
      int count=questionMapper.updateByPrimaryKey(newQuestion);
      if(count==0){return ServerResponse.createByErrorMessage("修改失败");}
        return ServerResponse.createBySuccess("修改成功");
    }

    @Override
    public ServerResponse deleteQuestionByQuestionId(Integer questionId) {
        Question question= questionMapper.selectQuestionByQuestionId(questionId);
        if(question==null){return ServerResponse.createByErrorMessage("找不到这个题目");}
        try{
            questionMapper.deleteByPrimaryKey(questionId);
        }catch (Exception e){
            return ServerResponse.createByErrorMessage("该题目有被引用，无法删除改题目");
        }
        return ServerResponse.createBySuccessMessage("删除题目成功");
    }

    @Override
    public ServerResponse<Question> getQuestionsByQuestionId(Integer questionId) {
        Question question= questionMapper.selectQuestionByQuestionId(questionId);
        if(question==null){return ServerResponse.createByErrorMessage("找不到这个题目");}
        return ServerResponse.createBySuccess(question);
    }

    @Override
    public ServerResponse<List<Question>> getAllQuestions() {
        return ServerResponse.createBySuccess(questionMapper.selectAllQuestions());
    }

    @Override
    public ServerResponse<List<Question>> getQuestionsByCondition(Integer subjectId, Integer difficulty, Integer questionTypeId, String keyPoint){
        if(difficulty==-1){
            difficulty=null;
        }
        if(questionTypeId==-1){
            questionTypeId=null;
        }
        if(keyPoint!=null&&keyPoint.equals("")){
            keyPoint=null;
        }
        List<Question> questionList=questionMapper.selectQuestionsByCondition(subjectId,difficulty,questionTypeId,keyPoint);
        return ServerResponse.createBySuccess(questionList);
    }

    @Override
    public ServerResponse batchImport(MultipartFile file) {
        List<Map<Integer,String>> alldata= ReadExcel.readExcelContentByList(file);
        for(Map<Integer,String> data:alldata ){

        }
        return ServerResponse.createBySuccess();
    }


}
