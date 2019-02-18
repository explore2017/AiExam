package com.explore.service;

import com.explore.common.ServerResponse;

public interface IExamStudentService {
    ServerResponse cancel(Integer exam_id,Integer student_id);
}
