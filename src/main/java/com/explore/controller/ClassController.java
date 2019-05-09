package com.explore.controller;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.pojo.*;
import com.explore.pojo.Class;
import com.explore.service.IClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassController {
    @Autowired
    IClassService classesService;

    /**
     * 查看班级详情
     */
    @GetMapping("/detail")
    public ServerResponse<List<Student>> getClassDetail(Integer id){
        ServerResponse<List<Student>>serverResponse=classesService.getClassDetail(id);
        return serverResponse;
    }


    /**
     * 查看所有班级
     */
    @GetMapping("/allclass")
    public ServerResponse getAllStudent(HttpSession session){
        if(session.getAttribute(Const.CURRENT_USER) instanceof Manager){
            return classesService.getClasses(Const.Manager,null);
        }else if(session.getAttribute(Const.CURRENT_USER) instanceof Teacher){
            Teacher teacher=(Teacher) session.getAttribute(Const.CURRENT_USER);
            return classesService.getClasses(Const.Teacher,teacher.getId());
        }else{
            return ServerResponse.createByErrorMessage("发生未知错误");
        }
    }

    /**
     * 增加班级
     */
    @PostMapping("/addclass")
    public ServerResponse addClass(@RequestBody Class classes){
        ServerResponse serverResponse=classesService.addClass(classes);
        return serverResponse;
    }

    /**
     * 删除班级
     */
    @DeleteMapping("/deleteclass/{id}")
    public ServerResponse deleteClass(@PathVariable("id") Integer id){
        ServerResponse serverResponse=classesService.deleteClass(id);
        return serverResponse;
    }

    /**
     * 删除班级中的学生
     */
    @DeleteMapping("/deletestudent/{studentId}")
    public ServerResponse deleteStudent(@PathVariable("studentId") Integer studentId,Integer classId){
        return classesService.deleteStudent(studentId,classId);
    }

    /**
     * 修改班级
     */
    @PutMapping("/reviseclass")
    public ServerResponse reviseClass(@RequestBody Class classes){
        ServerResponse serverResponse=classesService.reviseClass(classes);
        return serverResponse;
    }

    /**
     * 添加学生进班级
     */
    @PostMapping("/addstudent")
    public ServerResponse addStudent(@RequestBody StudentClass studentClass){
        ServerResponse serverResponse=classesService.addStudent(studentClass);
        return serverResponse;
    }
    @GetMapping("/key_point")
    public ServerResponse getKeyPoint(Integer classId){
        return classesService.getKeyPoint(classId);
    }


}
