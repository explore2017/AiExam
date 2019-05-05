package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.*;
import com.explore.vo.ExamBatchVo;
import com.explore.vo.PaperQuestionVo;

import java.util.List;

public interface IExamService {
    ServerResponse getExams(String role,Integer teacherId);

    List<ExamBatchVo> getExamBatchVoByStudentId(Integer id);

    ServerResponse save(Exam exam);

    ServerResponse autoCheck(ExamStudent examStudent, Paper paper, List<Question> questions);

    ServerResponse deleteExam(Integer examId);

    ServerResponse startReply(Integer studentId, Integer batchId);

    ServerResponse monitor(Integer studentId, Integer batchId, List<PaperQuestionVo> records,Boolean isSubmit);

    ServerResponse readPaper(Integer batchStudentId);

    ServerResponse readPaperSubmit(Integer batchStudentId,List<PaperQuestionVo> records);
}
