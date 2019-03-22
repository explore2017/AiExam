package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Class;
import com.explore.pojo.Student;

import java.util.List;

public interface IClassService {
    ServerResponse<List<Class>> getClasses();
    ServerResponse<List<Student>> getClassDetail(Integer id);
    ServerResponse addClass(Class classes);
    ServerResponse deleteClass(Integer id);
    ServerResponse reviseClass(Class classes);
    ServerResponse deleteStudent(Integer studentId,Integer classId);
}
