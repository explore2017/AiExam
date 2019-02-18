package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.dao.ExamStudentMapper;
import com.explore.pojo.ExamStudentKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamStudentServiceImpl implements IExamStudentService {

    @Autowired
    ExamStudentMapper examStudentMapper;

    @Override
    public ServerResponse cancel(Integer exam_id, Integer student_id) {
        ExamStudentKey examStudentKey = new ExamStudentKey();
        examStudentKey.setExamId(exam_id);
        examStudentKey.setStudentId(student_id);
        examStudentMapper.deleteByPrimaryKey(examStudentKey);
        return ServerResponse.createBySuccessMessage("取消成功");
    }
}
