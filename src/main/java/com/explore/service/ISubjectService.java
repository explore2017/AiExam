package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Subject;

import java.util.List;

public interface ISubjectService {
    ServerResponse<List<Subject>> Subject();

    ServerResponse addSubject(Subject subject);

    ServerResponse outSubject(Integer id);

    ServerResponse reviseSuject(Subject subject);
}
