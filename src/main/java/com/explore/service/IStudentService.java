package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Student;

public interface IStudentService {
    ServerResponse<Student> login(String sno, String password);

    ServerResponse revise(String sno, String oldPassword,String newPassword);

    ServerResponse reviseMessage(Integer id, String newPhone, String newEmail);

    ServerResponse resetPassword(String sno,String phone,String password);

    ServerResponse getExamVOs(Integer id);

    ServerResponse batchEnroll(Integer studentId, Integer batchId);

    ServerResponse batchCancel(Integer studentId, Integer batchId);

    ServerResponse getMyEnrollBatch(Integer studentId);

    ServerResponse getMyScore(Integer studentId);

    ServerResponse joinClass(Integer id,String classNo);

    ServerResponse exitClass(Integer studentId,Integer classId);
}
