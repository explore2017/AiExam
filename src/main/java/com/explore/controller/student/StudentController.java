package com.explore.controller.student;

import com.explore.common.Const;
import com.explore.common.ResponseCode;
import com.explore.common.ServerResponse;
import com.explore.pojo.*;
import com.explore.pojo.Class;
import com.explore.service.IClassService;
import com.explore.service.IExamService;
import com.explore.service.IExamStudentService;
import com.explore.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    IStudentService studentService;
    @Autowired
    IExamService examService;
    @Autowired
    IExamStudentService examStudentService;
    @Autowired
    IClassService classService;

    /**
     * 学生登录
     */
    @PostMapping("/login")
    public ServerResponse<Student> login(@RequestBody User user, HttpSession session) {
        ServerResponse<Student> serverResponse = studentService.login(user.getUsername(), user.getPassword());
        if (serverResponse.isSuccess()) {
            Student student = serverResponse.getData();
            session.setAttribute(Const.CURRENT_USER,student);
        }
        return serverResponse;
    }

    /**
     * 学生密码修改
     */
    @PutMapping("/password")
    public ServerResponse revise(String sno, String oldPassword, String newPassword) {
        ServerResponse serverResponse = studentService.revise(sno, oldPassword, newPassword);
        return serverResponse;
    }

    /**
     * 重置密码
     */
    @PutMapping("/resetPassword")
    public ServerResponse resetPassword(String sno,String phone,String password){
        ServerResponse serverResponse=studentService.resetPassword(sno,phone,password);
        return serverResponse;
    }

    /**
     * 学生信息修改
     */
    @PutMapping("/reviseMessage")
    public ServerResponse response(HttpSession session,String newPhone,String newEmail){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if(student==null) {
            return ServerResponse.createByErrorMessage("请重新登录");
        }
        int id=student.getId();
        ServerResponse serverResponse=studentService.reviseMessage(id,newPhone,newEmail);
        return serverResponse;
    }


    /**
     * 获取已参加考试
     */
    @GetMapping("/exam/batch")
    public ServerResponse<List<ExamStudent>>  examBatch(HttpSession session) {
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.createByErrorMessage("信息错误");
        }
//        List<ExamBatchVo> examBatchVoList = examService.getExamBatchVoByStudentId(student.getId());
        return examStudentService.getStudentExam(student.getId());
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/info")
    public ServerResponse<Student> info(HttpSession session) {
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录后尝试");
        }
        return ServerResponse.createBySuccess(student);
    }


    /**
     * 获取个人所属班级
     */
    @PostMapping("/myClass")
    public ServerResponse<List<Class>> getMyClass(HttpSession session) {
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录后尝试");
        }
        return classService.getClassesByStudentID(student.getId());
    }

    /**
     * 获取考试、批次列表
     * @param session
     * @return
     */
    @GetMapping("/exam")
    public ServerResponse getExams(HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录后尝试");
        }
        return studentService.getExamVOs(student.getId());
    }

    @PostMapping("/batch/enroll")
    public ServerResponse batchEnroll(@RequestBody Batch batch, HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录后尝试");
        }
        return studentService.batchEnroll(batch.getId(),student.getId());
    }


}