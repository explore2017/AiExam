package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Teacher;

public interface ITeacherService {
    ServerResponse<Teacher> login(String username, String password);

    ServerResponse revise(String username, String oldPassword, String newPassword);

}
