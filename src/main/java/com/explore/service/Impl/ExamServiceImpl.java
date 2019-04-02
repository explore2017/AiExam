package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.ExamMapper;
import com.explore.dao.QuestionMapper;
import com.explore.pojo.*;
import com.explore.service.IExamService;
import com.explore.vo.ExamBatchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

@Service
public class ExamServiceImpl implements IExamService {

    @Autowired
    ExamMapper examMapper;
    @Autowired
    PaperServiceImpl paperService;

    @Override
    public List<Exam> getExams() {
        return examMapper.selectExams();
    }

    @Override
    public List<ExamBatchVo> getExamBatchVoByStudentId(Integer student_id) {
        List<ExamBatchVo> exams = examMapper.selectExamBatchVoByStudentId(student_id);
        return exams;
    }

    @Override
    public ServerResponse save(Exam exam) {
        exam.setCreateTime(new Date());
        exam.setUpdateTime(new Date());
        int result = examMapper.insert(exam);
        if(result > 0)
            return ServerResponse.createBySuccess();
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse autoCheck(ExamStudent examStudent, Paper paper, List<Question> questions){
        Double score = 0.0;
        ServerResponse<List<Question>> answer =  paperService.getDetailsByPaperId(paper.getId());
        for(int i = 0;i<questions.size();i++){
            if(checkJudge(questions.get(i)) && questions.get(i).getAnswer().equals(answer.getData().get(i).getAnswer()))
                score += Double.valueOf(questions.get(i).getDefaultScore());
        }
        examStudent.setScore(score);
        return ServerResponse.createBySuccess(examStudent);
    }

    /**
     * 判断问题是否需要自动批改
     */
    public boolean checkJudge(Question question){
        if(question.getQuestionTypeId() == 3 || question.getQuestionTypeId() == 5 || question.getQuestionTypeId() == 6)
            return false;
        return true;
    }
}
