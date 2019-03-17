package com.explore.controller.manage;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
//import com.explore.dao.TeacherSubjectMapper;
import com.explore.dao.TeacherSubjectMapper;
import com.explore.pojo.Manager;
import com.explore.pojo.Student;
import com.explore.pojo.Teacher;
import com.explore.service.IManageService;

import com.explore.service.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    IManageService manageService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public ServerResponse login(@RequestBody String username, String password, HttpSession session) {
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
    public ServerResponse addStudent(@RequestBody Student student) {
        ServerResponse serverResponse = manageService.addStudent(student);
        return serverResponse;
    }

    /**
     * 学生删除
     */
    @DeleteMapping("/deleteStudent/{id}")
    public ServerResponse outStudent(@PathVariable("id") Integer id) {
        ServerResponse serverResponse = manageService.outStudent(id);
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
    public ServerResponse addTeacher(@RequestBody Teacher teacher) {
        String[] subjectId=teacher.getSubjectId().split(",");
        ServerResponse serverResponse = manageService.addTeacher(teacher,subjectId);
        return serverResponse;
    }

    /**
     * 老师删除
     */
    @DeleteMapping("/deleteTeacher/{id}")
    public ServerResponse outTeacher(@PathVariable("id") Integer id) {
        ServerResponse serverResponse = manageService.outTeacher(id);
        return serverResponse;
    }

    /**
     * 修改老师信息
     */
    @PutMapping("/reviseTeacher")
    public ServerResponse reviseTeacher(@RequestBody Teacher teacher) {
        String[] subjectId=teacher.getSubjectId().split(",");
        ServerResponse serverResponse = manageService.reviseTeacher(teacher,subjectId);
        return serverResponse;
    }


}
