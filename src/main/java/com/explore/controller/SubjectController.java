package com.explore.controller;

import com.explore.common.ServerResponse;
import com.explore.dao.TeacherSubjectMapper;
import com.explore.pojo.Subject;
import com.explore.service.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    ISubjectService subjectService;


    /**
     * 查看所有课程
     */
    @GetMapping("/allSubject")
    public ServerResponse<List<Subject>> getAllStudent(Subject subject){
        ServerResponse<List<Subject>> serverResponse=subjectService.Subject();
        return serverResponse;
    }

    /**
     * 增加课程
     */
    @PostMapping("/addSubject")
    public ServerResponse addSubject(Subject subject){
        ServerResponse serverResponse=subjectService.addSubject(subject);
        return serverResponse;
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/deleteSubject")
    public ServerResponse outSubject(Subject subject){
        ServerResponse serverResponse=subjectService.outSubject(subject);
        return serverResponse;
    }

    /**
     * 增加课程
     */
    @PutMapping("/reviseSubject")
    public ServerResponse reviseSuject(Subject subject){
        ServerResponse serverResponse=subjectService.reviseSuject(subject);
        return serverResponse;
    }
}