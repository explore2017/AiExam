package com.explore.controller.student;

import com.explore.common.Const;
import com.explore.common.ResponseCode;
import com.explore.common.ServerResponse;
import com.explore.pojo.ExamStudent;
import com.explore.pojo.Student;
import com.explore.pojo.User;
import com.explore.service.IClassService;
import com.explore.service.IExamService;
import com.explore.service.IExamStudentService;
import com.explore.service.IStudentService;
import com.explore.service.Impl.StudentServiceImpl;
import com.explore.vo.ExamBatchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.xml.ws.RequestWrapper;
import java.util.Date;
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
    @GetMapping("/login")
    public ServerResponse<Student> login(String sno, String password, HttpSession session) {
        ServerResponse<Student> serverResponse = studentService.login(sno, password);
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
    @PostMapping("/myclass")
    public ServerResponse<List<Class>> getMyClass(HttpSession session) {
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录后尝试");
        }

        ServerResponse serverResponse = classService.getClassesByStudentID(student.getId());

        return serverResponse;
    }




}