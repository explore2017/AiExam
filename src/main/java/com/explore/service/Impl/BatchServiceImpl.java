package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.*;
import com.explore.pojo.*;
import com.explore.service.IBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BatchServiceImpl implements IBatchService {

    @Autowired
    BatchMapper batchMapper;
    @Autowired
    ExamStudentMapper examStudentMapper;
    @Autowired
    BatchStudentMapper batchStudentMapper;
    @Autowired
    ExamMapper examMapper;
    @Autowired
    StudentMapper studentMapper;

    @Override
    public ServerResponse getBatchesByExamId(Integer exam_id) {
        List<Batch> batchList=batchMapper.selectBatchesByExamId(exam_id);
        List<Map<String,Object>>  data=new ArrayList<>();
        for(Batch batch:batchList){
            Map<String,Object> single=new HashMap<>();
            single.put("number", batchStudentMapper.getBatchSelelectedNumberByBatchId(batch.getId()));
            single.put("batch",batch);
            data.add(single);
        }
        return  ServerResponse.createBySuccess(data);
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
        Exam exam =examMapper.selectByPrimaryKey(batch.getExamId());
        if(exam==null){return ServerResponse.createByErrorMessage("发生未知错误");}
        batch.setPaperId(exam.getPaperId());
       if(batchMapper.insert(batch)>0) {
           return ServerResponse.createBySuccessMessage("添加批次成功");
       }
        return ServerResponse.createByErrorMessage("添加批次失败");
    }

    @Override
    public ServerResponse delBacth(Integer batchId) {
        batchStudentMapper.deleteByBatchId(batchId);
        batchMapper.deleteByPrimaryKey(batchId);
        return ServerResponse.createBySuccessMessage("删除批次成功");
    }

    @Override
    public ServerResponse getBatchDetails(Integer batchId) {
        List<Map<String,Object>> data=new ArrayList<>();
        List<BatchStudent> batchStudentList=batchStudentMapper.selectByBatchId(batchId);
        for(BatchStudent batchStudent:batchStudentList){
            Map<String,Object> single=new HashMap<>();
           Student student=studentMapper.selectByPrimaryKey(batchStudent.getStudentId());
           if(student==null){return ServerResponse.createByErrorMessage("发生未知错误");}
           student.setPassword("");
           student.setPhone("");
           student.setEmail("");
           single.put("student",student);
           single.put("batchStudent",batchStudent);
           data.add(single);
        }
        return ServerResponse.createBySuccess(data);
    }

    @Override
    public ServerResponse deleteBatchStudent(Integer studentId, Integer batchId) {
        if(batchStudentMapper.cancel(studentId,batchId)>0){
          return   ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }
}
