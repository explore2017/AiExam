package com.explore.service;

import com.explore.pojo.Exam;
import com.explore.vo.ExamBatchVo;

import java.util.List;

public interface IExamService {
    List<Exam> getExams();

    List<ExamBatchVo> getExamBatchVoByStudentId(Integer id);
}
