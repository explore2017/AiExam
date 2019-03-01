package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Exam;

public interface IExamStudentService {
    ServerResponse cancel(Integer exam_id,Integer student_id);
    ServerResponse signExam(Integer exam_id,Integer student_id);
    ServerResponse setStudentExamScore(Integer exam_id,Integer student_id,Double score);
    ServerResponse getStudentExam(Integer student_id);
}
