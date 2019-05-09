package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Teacher;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface ITeacherService {
    ServerResponse<Teacher> login(String username, String password);

    ServerResponse revise(String username, String oldPassword, String newPassword);

    ServerResponse getStudent(String sno);

    ServerResponse batchImport(MultipartFile file);
}
