package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.ExamMapper;
import com.explore.pojo.Exam;
import com.explore.service.IExamService;
import com.explore.vo.ExamBatchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExamServiceImpl implements IExamService {

    @Autowired
    ExamMapper examMapper;

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

}
