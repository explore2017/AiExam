package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.dao.BatchMapper;
import com.explore.pojo.Batch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BatchServiceImpl implements IBatchService {

    @Autowired
    BatchMapper batchMapper;

    @Override
    public List<Batch> getBatchesByExamId(Integer exam_id) {
        return batchMapper.selectBatchesByExamId(exam_id);
    }

    @Override
    public ServerResponse  enroll(Integer batch_id,Integer student_id) {
        //TODO 校验是否报名过该考试
//        int count = batchStudentMapper.checkHasEnroll(batch_id,student_id);
//        if(count>0){
//            return ServerResponse.createByErrorMessage("你已经报名过该考试");
//        }
//        BatchStudent batchStudent = new BatchStudent();
//        batchStudent.setStudentId(student_id);
//        batchStudent.setBatchId(batch_id);
//        batchStudent.setCreateTime(new Date());
//        batchStudent.setStatus(0);
        //重复报名
        try {
            //batchStudentMapper.insert(batchStudent);
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
