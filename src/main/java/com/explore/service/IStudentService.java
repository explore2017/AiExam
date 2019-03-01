package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Student;
import com.explore.pojo.Teacher;

public interface IStudentService {
    ServerResponse<Student> login(String sno, String password);

    ServerResponse revise(String sno, String oldPassword,String newPassword);

    ServerResponse reviseMessage(int id, String newPhone, String newEmail);

    ServerResponse resetPassword(String sno,String phone,String password);
}
