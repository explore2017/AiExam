package com.explore.controller.teacher;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.form.PasswordForm;
import com.explore.pojo.Student;
import com.explore.pojo.Teacher;
import com.explore.pojo.User;
import com.explore.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    ITeacherService teacherService;


    /**
     * 老师登录
     */
    @PostMapping("/login")
    public ServerResponse login(@RequestBody User user,  HttpSession session) {
        ServerResponse<Teacher> serverResponse = teacherService.login(user.getUsername(),user.getPassword());
        if (serverResponse.isSuccess()) {
            Teacher teacher = serverResponse.getData();
            session.setAttribute(Const.CURRENT_USER,teacher);
        }
        return serverResponse;
    }


    /**
     * 老师密码修改
     */
    @PutMapping("/password")
    public ServerResponse revise(@RequestBody PasswordForm passwordForm,HttpSession session) {
        Teacher teacher=(Teacher)session.getAttribute(Const.CURRENT_USER);
        return teacherService.revise(teacher.getUsername(), passwordForm.getOldPassword(), passwordForm.getNewPassword());
    }

    /**
     * 查看一个学生
     */
    @GetMapping("/searchstudent")
    public ServerResponse<Student> getStudent(String sno) {
        return  teacherService.getStudent(sno);

    }

}
