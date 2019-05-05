package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Subject;

import java.util.List;

public interface ISubjectService {
    ServerResponse<List<Subject>> Subject(String role,Integer teacherId);

    ServerResponse addSubject(Subject subject);

    ServerResponse outSubject(Integer id);

    ServerResponse reviseSubject(Subject subject);

    ServerResponse getSubjectQuestion(Integer subjectId);
}
