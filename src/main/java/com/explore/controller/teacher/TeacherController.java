package com.explore.controller.teacher;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.pojo.Teacher;
import com.explore.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    ITeacherService teacherService;


    /**
     * 老师登录
     */
    @GetMapping("/Login")
    public ServerResponse login(String username, String password,HttpSession session) {
        ServerResponse<Teacher> serverResponse = teacherService.login(username, password);
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
    public ServerResponse revise(String username, String oldPassword, String newPassword) {
        ServerResponse serverResponse = teacherService.revise(username, oldPassword, newPassword);
        return serverResponse;
    }
}
