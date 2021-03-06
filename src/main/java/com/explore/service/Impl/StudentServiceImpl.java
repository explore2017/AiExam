package com.explore.service.Impl;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.dao.*;
import com.explore.pojo.*;
import com.explore.pojo.Class;
import com.explore.service.IStudentService;
import com.explore.vo.BatchStudentVO;
import com.explore.vo.BatchVO;
import com.explore.vo.ExamVO;
import com.explore.vo.PaperQuestionVo;
import io.swagger.models.auth.In;
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
    @Autowired
    ClassMapper classMapper;
    @Autowired
    StudentClassMapper studentClassMapper;
    @Autowired
    ClassServiceImpl classService;
    @Autowired
    PaperRecordMapper paperRecordMapper;
    @Autowired
    ExamServiceImpl examService;

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
        //TODO
        Student student = studentMapper.login(sno, oldPassword);
        if (student == null) {
            return ServerResponse.createByErrorMessage("原密码错误");
        }
        student.setPassword(newPassword);
        student.setUpdateTime(new Date());
        int count = studentMapper.updateByPrimaryKey(student);
        if (count == 1) {
            return ServerResponse.createBySuccessMessage("修改成功");
        }
        return ServerResponse.createByErrorMessage("修改失败");
    }

    @Override
    public ServerResponse reviseMessage(Integer id, String newPhone, String newEmail) {
        Student student = studentMapper.selectByPrimaryKey(id);
        student.setPhone(newPhone);
        student.setEmail(newEmail);
        student.setUpdateTime(new Date());
        int count = studentMapper.updateByPrimaryKeySelective(student);
        if (count == 1) {
            return ServerResponse.createBySuccessMessage("修改成功");
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
            examVO.setHasEnroll(false);
            List<Batch> batches = batchMapper.selectBatchesByExamId(exam.getId());
            //判断是否报名了
            List<BatchVO> batchVOs = new LinkedList<>();
            for (Batch batch:batches) {
                BatchVO batchVO = modelMapper.map(batch,BatchVO.class);
                batchVO.setHasSelected(false);
                batchVO.setIsFull(false);
                int count = batchStudentMapper.checkHasSelected(studentId,batch.getId());
                if (count>0){
                    batchVO.setHasSelected(true);
                    examVO.setHasEnroll(true);
                }
                int selectedNumber = batchStudentMapper.getBatchSelelectedNumberByBatchId(batch.getId());
                batchVO.setSelectedNumber(selectedNumber);
                //批次满人
                if (batchVO.getMaxNumber()==selectedNumber){
                    batchVO.setIsFull(true);
                }
                batchVOs.add(batchVO);
            }
            examVO.setItsClass(classMapper.selectByPrimaryKey(exam.getClassId()));
            examVO.setBatches(batchVOs);
            examVOs.add(examVO);
        }
        return ServerResponse.createBySuccess(examVOs);
    }

    @Override
    public ServerResponse batchEnroll(Integer batchId, Integer studentId) {
        //验证考试是否开始了
        Batch checkTimeBatch = batchMapper.selectByPrimaryKey(batchId);
        if(checkTimeBatch.getStartTime().compareTo(new Date())<=0){
            return ServerResponse.createByErrorMessage("该批次已开始，报名失败！");
        }
        //验证是否已经报名了该考试
        int count = batchStudentMapper.checkHasEnroll(studentId,batchId);
        if (count>0){
            return ServerResponse.createByErrorMessage("报名失败，你已经报名过该考试!");
        }
        //判断是否满人
        Batch batch = batchMapper.selectByPrimaryKey(batchId);
        int selectedNumber = batchStudentMapper.getBatchSelelectedNumberByBatchId(batchId);
        if (selectedNumber>=batch.getMaxNumber()){
            return ServerResponse.createByErrorMessage("报名失败，批次人数已满!");
        }
        BatchStudent batchStudent = new BatchStudent();
        batchStudent.setStudentId(studentId);
        batchStudent.setBatchId(batchId);
        Date now =  new Date();
        batchStudent.setCreateTime(now);
        batchStudent.setUpdateTime(now);
        batchStudent.setStatus(0);
        batchStudentMapper.insert(batchStudent);
        return ServerResponse.createBySuccessMessage("报名成功");
    }

    @Override
    public ServerResponse batchCancel(Integer batchId, Integer studentId) {
        //TODO 批次开始后不能删除（包括已考试）
        BatchStudent batchStudent = batchStudentMapper.selectByStudentIdAndBatchId(studentId,batchId);
        int status = batchStudent.getStatus();
        if (status!= Const.BATCH_STUDENT_STATUS.NOT_SIGN.getStatus()){
            return ServerResponse.createByErrorMessage("取消失败，当前状态不允许取消");
        }
        int count = batchStudentMapper.cancel(studentId,batchId);
        if (count>0){
            return ServerResponse.createBySuccessMessage("取消成功");
        }
        return ServerResponse.createByErrorMessage("取消异常，请检查是否成功处理");
    }

    @Override
    public ServerResponse getMyEnrollBatch(Integer studentId) {
        //TODO 考试结束时间之前
        List<BatchStudent> list = batchStudentMapper.selectByStudentId(studentId);
        List<BatchStudentVO> batchStudentVOList = new ArrayList<>();
        for (BatchStudent batchStudent : list) {
            BatchStudentVO batchStudentVO = modelMapper.map(batchStudent,BatchStudentVO.class);
            Batch batch = batchMapper.selectByPrimaryKey(batchStudent.getBatchId());
            batchStudentVO.setBatch(batch);
            if(batchStudentMapper.checkExamEnd(batch.getExamId())>0){
                batchStudentVO.setExamPaperStatus(false);
            }else {
                batchStudentVO.setExamPaperStatus(true);
            }
            batchStudentVO.setExam(examMapper.selectByPrimaryKey(batch.getExamId()));
            batchStudentVOList.add(batchStudentVO);

        }
        return ServerResponse.createBySuccess(batchStudentVOList);
    }


    @Override
    public ServerResponse getMyScore(Integer studentId) {
        //考试提交状态以后的
        List<BatchStudent> list = batchStudentMapper.selectAfterFinishedByStudentId(studentId);
        List<BatchStudentVO> batchStudentVOList = new ArrayList<>();
        for (BatchStudent batchStudent : list) {
            BatchStudentVO batchStudentVO = modelMapper.map(batchStudent,BatchStudentVO.class);
            Batch batch = batchMapper.selectByPrimaryKey(batchStudent.getBatchId());
            batchStudentVO.setBatch(batch);
            batchStudentVO.setExam(examMapper.selectByPrimaryKey(batch.getExamId()));
            batchStudentVOList.add(batchStudentVO);
        }
        return ServerResponse.createBySuccess(batchStudentVOList);
    }

    @Override
    public ServerResponse joinClass(Integer id,String classNo) {
        Class class1=classMapper.checkHasEnroll(classNo.toUpperCase());
        if(class1==null){
            return  ServerResponse.createByErrorMessage("找不到该班级号对应的班级");
        }
            if(studentClassMapper.check(id,class1.getId())!=null){
                return  ServerResponse.createByErrorMessage("已加入班级");
            }
        StudentClass studentClass=new StudentClass();
        studentClass.setClassId(class1.getId());
        studentClass.setStudentId(id);
       ServerResponse serverResponse= classService.addStudent(studentClass);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccessMessage("加入班级成功");
        }
        return ServerResponse.createByErrorMessage("加入班级失败");
    }

    @Override
    public ServerResponse exitClass(Integer studentId,Integer classId) {
        ServerResponse serverResponse= classService.deleteStudent(studentId,classId);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccessMessage("退出班级成功");
        }
        return ServerResponse.createByErrorMessage("退出班级失败");
    }

    @Override
    public ServerResponse getExamPaper(Integer studentId,Integer batchId) {
        Batch batch=batchMapper.selectByPrimaryKey(batchId);
        if(batch==null){
            return  ServerResponse.createByErrorMessage("发生未知错误");
        }
        if(batchStudentMapper.checkExamEnd(batch.getExamId())>0){
            return  ServerResponse.createByErrorMessage("未可以查看试卷");
        }
        List<PaperRecord> paperRecords=paperRecordMapper.selectByStudentIdAndBatchId(studentId,batchId);
        List<PaperQuestionVo> paperQuestionVos =examService.packagePaperRecordToPaperQuestionVo(paperRecords,true);
        return ServerResponse.createBySuccess(paperQuestionVos);
    }

}
