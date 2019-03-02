package com.explore.controller.manage;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.pojo.Manager;
import com.explore.pojo.Student;
import com.explore.pojo.Teacher;
import com.explore.service.IManageService;
import com.explore.service.IStudentService;
import com.explore.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    IManageService manageService;

    /**
     * 管理员登录
     */
    @GetMapping("/login")
    public ServerResponse login(String username, String password, HttpSession session) {
        ServerResponse<Manager> serverResponse = manageService.login(username, password);
        if (serverResponse.isSuccess()) {
            Manager manager = serverResponse.getData();
            session.setAttribute(Const.CURRENT_USER,manager);
        }
        return serverResponse;
    }

    /**
     * 管理员密码修改
     */
    @PutMapping("/password")
    public ServerResponse revise(String username, String oldPassword, String newPassword) {
        ServerResponse serverResponse = manageService.revise(username, oldPassword, newPassword);
        return serverResponse;
    }

    /**
     * 查看所有学生数据
     */
    @GetMapping("/Students")
    public ServerResponse<List<Student>> getAllStudent(Student student){
        ServerResponse<List<Student>> serverResponse=manageService.Students();
        return serverResponse;
    }

    /**
     * 学生添加
     */
    @PostMapping("/insertStudent")
    public ServerResponse addStudent(Student student) {
        ServerResponse serverResponse = manageService.addStudent(student);
        return serverResponse;
    }

    /**
     * 学生删除
     */
    @DeleteMapping("/deleteStudent")
    public ServerResponse outStudent(Student student) {
        ServerResponse serverResponse = manageService.outStudent(student);
        return serverResponse;
    }

    /**
     * 修改学生信息
     */
    @PutMapping("/reviseStudent")
    public ServerResponse reviseStudent(Student student) {
        ServerResponse serverResponse = manageService.reviseStudent(student);
        return serverResponse;
    }

    /**
     * 查看所有老师数据
     */
    @GetMapping("/Teachers")
    public ServerResponse<List<Teacher>> getAllStudent(Teacher teacher){
        ServerResponse<List<Teacher>> serverResponse=manageService.Teachers();
        return serverResponse;
    }

    /**
     * 老师添加
     */
    @PostMapping("/insertTeacher")
    public ServerResponse addTeacher(Teacher teacher) {
        ServerResponse serverResponse = manageService.addTeacher(teacher);
        return serverResponse;
    }

    /**
     * 老师删除
     */
    @DeleteMapping("/deleteTeacher")
    public ServerResponse outTeacher(Teacher teacher) {
        ServerResponse serverResponse = manageService.outTeacher(teacher);
        return serverResponse;
    }

    /**
     * 修改老师信息
     */
    @PutMapping("/reviseTeacher")
    public ServerResponse reviseTeacher(Teacher teacher) {
        ServerResponse serverResponse = manageService.reviseTeacher(teacher);
        return serverResponse;
    }


}
