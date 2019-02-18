package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Student;

public interface IStudentService {
    ServerResponse<Student> login(String sno, String password);
}
