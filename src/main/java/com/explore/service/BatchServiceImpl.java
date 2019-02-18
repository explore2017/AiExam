package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.dao.BatchMapper;
import com.explore.dao.ExamStudentMapper;
import com.explore.pojo.Batch;
import com.explore.pojo.ExamStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BatchServiceImpl implements IBatchService {

    @Autowired
    BatchMapper batchMapper;
    @Autowired
    ExamStudentMapper examStudentMapper;

    @Override
    public List<Batch> getBatchesByExamId(Integer exam_id) {
        return batchMapper.selectBatchesByExamId(exam_id);
    }

    @Override
    public ServerResponse  enroll(Integer batch_id,Integer student_id) {
        int exam_id = batchMapper.selectExamIdByBatchId(batch_id);
        //校验是否报名过该考试
        int count = examStudentMapper.checkHasEnroll(exam_id,student_id);
        if(count>0){
            return ServerResponse.createByErrorMessage("你已经报名过该考试");
        }
        ExamStudent examStudent = new ExamStudent();
        examStudent.setExamId(exam_id);
        examStudent.setStudentId(student_id);
        examStudent.setBatchId(batch_id);
        examStudent.setCreateTime(new Date());
        examStudent.setStatus(0);
        //重复报名
        try {
            examStudentMapper.insert(examStudent);
        }catch (Exception e){
            return ServerResponse.createByErrorMessage("报名失败");
        }
        return ServerResponse.createBySuccessMessage("报名成功");
    }

    @Override
    public ServerResponse save(Batch batch) {
        batchMapper.insert(batch);
        return ServerResponse.createBySuccessMessage("创建批次成功");
    }
}
