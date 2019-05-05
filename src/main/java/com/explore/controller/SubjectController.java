package com.explore.controller;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.dao.TeacherSubjectMapper;
import com.explore.pojo.Manager;
import com.explore.pojo.Subject;
import com.explore.pojo.Teacher;
import com.explore.service.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    ISubjectService subjectService;


    /**
     * 查看所有课程
     */
    @GetMapping
    public ServerResponse<List<Subject>> getAllSubject(HttpSession session){
        if(session.getAttribute(Const.CURRENT_USER) instanceof Manager){
            return subjectService.Subject(Const.Manager,null);
        }else if(session.getAttribute(Const.CURRENT_USER) instanceof Teacher){
            Teacher teacher=(Teacher) session.getAttribute(Const.CURRENT_USER);
            return subjectService.Subject(Const.Teacher,teacher.getId());
        }else{
            return ServerResponse.createByErrorMessage("发生未知错误");
        }
    }

    /**
     * 增加课程
     */
    @PostMapping("/addSubject")
    public ServerResponse addSubject(@RequestBody Subject subject){
        ServerResponse serverResponse=subjectService.addSubject(subject);
        return serverResponse;
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/{subjectId}")
    public ServerResponse outSubject(@PathVariable("subjectId") Integer id){
        ServerResponse serverResponse=subjectService.outSubject(id);
        return serverResponse;
    }

    /**
     * 修改课程
     */
    @PutMapping
    public ServerResponse reviseSubject(@RequestBody  Subject subject){
        ServerResponse serverResponse=subjectService.reviseSubject(subject);
        return serverResponse;
    }
    /**
     * 查看科目下试题
     */
    @GetMapping("/question")
    public ServerResponse<List<Subject>> getSubjectQuestion(Integer subjectId){
        return subjectService.getSubjectQuestion(subjectId);
    }


}
