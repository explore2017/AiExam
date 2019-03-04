package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.*;
import com.explore.vo.ExamBatchVo;

import java.util.List;

public interface IExamService {
    List<Exam> getExams();

    List<ExamBatchVo> getExamBatchVoByStudentId(Integer id);

    ServerResponse save(Exam exam);

    ServerResponse autoCheck(ExamStudent examStudent, Paper paper, List<Question> questions);
}
