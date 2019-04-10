package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.ExamStudentMapper;
import com.explore.pojo.ExamStudent;
import com.explore.pojo.ExamStudentKey;
import com.explore.service.IExamStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    @Override
    public ServerResponse signExam(Integer exam_id, Integer student_id) {
        ExamStudent examStudent = new ExamStudent();
        Date date=new Date();
        examStudent.setExamId(exam_id);
        return null;
    }

    @Override
    public ServerResponse setStudentExamScore(Integer exam_id, Integer student_id, Double score) {
        ExamStudentKey examStudentKey = new ExamStudentKey();
        examStudentKey.setExamId(exam_id);
        examStudentKey.setStudentId(student_id);
        ExamStudent examStudent=examStudentMapper.selectByPrimaryKey(examStudentKey);
        if(examStudent==null){return  ServerResponse.createByErrorMessage("找不到该学生考试");}
        int count= examStudentMapper.updateScore(examStudentKey,score);
        if(count==1){return  ServerResponse.createBySuccessMessage("修改成绩成功");}
        return ServerResponse.createByErrorMessage("修改成绩失败");
    }

    /**
     * 获得学生考试
     * @param student_id
     * @return
     */
    @Override
    public ServerResponse<List<ExamStudent>> getStudentExam(Integer student_id) {
        return ServerResponse.createBySuccessMessage("查询成功",examStudentMapper.selectByStudentId(student_id));
    }
}
