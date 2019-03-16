package com.explore.controller;

import com.explore.common.Const;
import com.explore.common.ResponseCode;
import com.explore.common.ServerResponse;
import com.explore.pojo.*;
import com.explore.service.IBatchService;
import com.explore.service.IExamService;
import com.explore.service.IExamStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    IExamService examService;
    @Autowired
    IBatchService batchService;
    @Autowired
    IExamStudentService examStudentService;

    /**
     * 获取所有考试
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse list(){
        List<Exam> exams = examService.getExams();
        return ServerResponse.createBySuccess(exams);
    }

    /**
     * 通过考试id列出所有批次
     */
    @ResponseBody
    @RequestMapping("/batch/{exam_id}")
    public ServerResponse batch(@PathVariable("exam_id") Integer exam_id){
        List<Batch> batches = batchService.getBatchesByExamId(exam_id);

        return ServerResponse.createBySuccess(batches);
    }

    /**
     * 考试批次报名
     */
    @RequestMapping("/batch/enroll/{batch_id}")
    @ResponseBody
    public ServerResponse enroll(@PathVariable("batch_id")Integer batch_id, HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if(student==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后尝试");
        }

        return batchService.enroll(batch_id,student.getId());
    }

    /**
     * 取消考试
     */
    @RequestMapping("/batch/cancel/{exam_id}")
    @ResponseBody
    public ServerResponse cancel(@PathVariable("exam_id")Integer exam_id,HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if(student==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后尝试");
        }
        return examStudentService.cancel(exam_id,student.getId());
    }

    /**
     * 添加考试
     */
    @PostMapping
    @ResponseBody
    public ServerResponse add(@RequestBody Exam exam){
        ServerResponse serverResponse = examService.save(exam);
        return serverResponse;
//        return  null;
    }


    /**
     * 添加考试批次
     */
    @PostMapping("batch")
    @ResponseBody
    public ServerResponse addBatch(Batch batch){
        ServerResponse serverResponse = batchService.save(batch);
        return serverResponse;
    }

    /**
     * 删除考试批次
     */
    @DeleteMapping("batch")
    @ResponseBody
    public ServerResponse delBatch(Batch batch){
        ServerResponse serverResponse = batchService.delBacth(batch);
        return serverResponse;
    }

    /**
     * 自动批改
     */
    @GetMapping("batch")
    @ResponseBody
    public ServerResponse autoCheck(ExamStudent examStudent, Paper paper, List<Question> questions){
        ServerResponse serverResponse = examService.autoCheck(examStudent, paper, questions);
        return serverResponse;
    }
}
