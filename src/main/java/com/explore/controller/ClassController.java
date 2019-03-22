package com.explore.controller;

import com.explore.common.ServerResponse;
import com.explore.pojo.Class;
import com.explore.pojo.Student;
import com.explore.service.IClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ServerResponse<List<Class>> getAllStudent(){
        ServerResponse<List<Class>> serverResponse=classesService.getClasses();
        return serverResponse;
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
        ServerResponse serverResponse=classesService.deleteStudent(studentId,classId);
        return serverResponse;
    }

    /**
     * 修改班级
     */
    @PutMapping("/reviseclass")
    public ServerResponse reviseClass(Class classes){
        ServerResponse serverResponse=classesService.reviseClass(classes);
        return serverResponse;
    }

}
