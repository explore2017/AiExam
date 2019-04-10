package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.BatchMapper;
import com.explore.dao.BatchStudentMapper;
import com.explore.dao.ExamMapper;
import com.explore.dao.StudentMapper;
import com.explore.pojo.Batch;
import com.explore.pojo.Exam;
import com.explore.pojo.Student;
import com.explore.service.IStudentService;
import com.explore.vo.BatchVO;
import com.explore.vo.ExamVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    StudentMapper studentMapper;
    @Autowired
    ExamMapper examMapper;
    @Autowired
    BatchMapper batchMapper;
    @Autowired
    BatchStudentMapper batchStudentMapper;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public ServerResponse<Student> login(String sno, String password) {
        Student student = studentMapper.login(sno, password);
        if (student == null) {
            return ServerResponse.createByErrorMessage("学号或密码错误,请重新尝试");
        }
        student.setPassword("");
        return ServerResponse.createBySuccessMessage("登录成功", student);
    }

    @Override
    public ServerResponse revise(String sno, String oldPassword, String newPassword) {
        Student student = studentMapper.login(sno, oldPassword);
        if (student == null) {
            return ServerResponse.createByErrorMessage("学号或密码错误");
        }
        student.setPassword(newPassword);
        Date update_time = new Date();
        student.setUpdateTime(update_time);
        int count = studentMapper.updateByPrimaryKey(student);
        if (count == 1) {
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("修改失败");
    }

    @Override
    public ServerResponse reviseMessage(int id, String newPhone, String newEmail) {
        Student student = studentMapper.selectByPrimaryKey(id);
        student.setPhone(newPhone);
        student.setEmail(newEmail);
        Date update_time = new Date();
        student.setUpdateTime(update_time);
        int count = studentMapper.updateByPrimaryKeySelective(student);
        if (count == 1) {
            return ServerResponse.createBySuccessMessage("修改信息成功");
        }
        return ServerResponse.createByErrorMessage("修改失败");
    }

    @Override
    public ServerResponse resetPassword(String sno,String phone,String password) {
        Student student1=studentMapper.selectSno(sno);
        if(student1==null){
            return ServerResponse.createByErrorMessage("学号错误");
        }
        if(student1.getPhone().equals(phone)){
            student1.setPassword(password);
            int count = studentMapper.updateByPrimaryKeySelective(student1);
            if (count == 1) {
                return ServerResponse.createBySuccessMessage("修改信息成功");
            }
        }
        return ServerResponse.createByErrorMessage("手机号输入错误");
    }

    @Override
    public ServerResponse getExamVOs(Integer studentId) {
        //TODO 获取符合报名时间段内的考试
        List<Exam> exams = examMapper.selectExamsByStudentId(studentId);
        List<ExamVO> examVOs = new LinkedList<>();
        for (Exam exam:exams) {
            ExamVO examVO = modelMapper.map(exam,ExamVO.class);
            List<Batch> batches = batchMapper.selectBatchesByExamId(exam.getId());
            //判断是否报名了
            List<BatchVO> batchVOs = new LinkedList<>();
            for (Batch batch:batches) {
                BatchVO batchVO = modelMapper.map(batch,BatchVO.class);
                int count = batchStudentMapper.checkHasSelected(studentId,batch.getId());
                if (count>0){
                    batchVO.setHasSelected(true);
                    examVO.setHasEnroll(true);
                }else{
                    batchVO.setHasSelected(false);
                    examVO.setHasEnroll(false);
                }
                batchVOs.add(batchVO);
            }
            examVO.setBatches(batchVOs);
            examVOs.add(examVO);
        }
        return ServerResponse.createBySuccess(examVOs);
    }

}
