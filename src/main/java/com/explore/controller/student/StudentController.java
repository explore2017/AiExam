package com.explore.controller.student;

import com.explore.common.Const;
import com.explore.common.ResponseCode;
import com.explore.common.ServerResponse;
import com.explore.pojo.Student;
import com.explore.service.IExamService;
import com.explore.service.IStudentService;
import com.explore.vo.ExamBatchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    IStudentService studentService;
    @Autowired
    IExamService examService;

    /**
     * 登录页面跳转
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * 学生登录
     */
    @RequestMapping("/login")
    public String login(String sno, String password, HttpSession session){
        ServerResponse<Student> serverResponse = studentService.login(sno,password);
        if (serverResponse.isSuccess()){
            Student student = serverResponse.getData();
            session.setAttribute(Const.CURRENT_USER,student);
        }
        return "student/index";
    }

    /**
     * 获取已参加考试
     */
    @RequestMapping("/exam/batch")
    public String examBatch(HttpSession session, Model model){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student==null){
            return "403";
        }
        List<ExamBatchVo> examBatchVoList = examService.getExamBatchVoByStudentId(student.getId());
        model.addAttribute("exams",examBatchVoList);
        return "student/exam";
    }

    /**
     * 获取个人信息
     */
    @RequestMapping("/info")
    @ResponseBody
    public ServerResponse<Student> info(HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后尝试");
        }
        return ServerResponse.createBySuccess(student);
    }

}