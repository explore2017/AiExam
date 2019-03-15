package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Manager;
import com.explore.pojo.Student;
import com.explore.pojo.Teacher;

import java.util.List;

public interface IManageService {
    ServerResponse<Manager> login(String username, String password);

    ServerResponse revise(String username, String oldPassword, String newPassword);

    ServerResponse<List<Student>> Students();

    ServerResponse addStudent(Student student);

    ServerResponse outStudent(int id);

    ServerResponse reviseStudent(Student student);

    ServerResponse<List<Teacher>> Teachers();

    ServerResponse addTeacher(Teacher teacher,int[] subject);

    ServerResponse outTeacher(int id);

    ServerResponse reviseTeacher(Teacher teacher,int[] subject);

}
