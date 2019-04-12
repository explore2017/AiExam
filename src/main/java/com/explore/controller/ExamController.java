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
        return examService.getExams();
    }

    /**
     * 通过考试id列出所有批次
     */
    @ResponseBody
    @GetMapping("/batch")
    public ServerResponse batch( Integer examId){
        return batchService.getBatchesByExamId(examId);
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
     * 取消一个学生的考试
     */
    @DeleteMapping("/batch/details/{studentId}")
    @ResponseBody
    public ServerResponse deleteBatchStudent(@PathVariable("studentId")Integer studentId,Integer batchId){
        return batchService.deleteBatchStudent(studentId,batchId);
    }

    /**
     * 添加考试
     */
    @PostMapping
    @ResponseBody
    public ServerResponse add(@RequestBody Exam exam){
        ServerResponse serverResponse = examService.save(exam);
        return serverResponse;
    }


    /**
     * 添加考试批次
     */
    @PostMapping("batch")
    @ResponseBody
    public ServerResponse addBatch(@RequestBody  Batch batch){
        return batchService.save(batch);
    }

    /**
     * 删除考试批次
     */
    @DeleteMapping("batch/{batchId}")
    @ResponseBody
    public ServerResponse delBatch(@PathVariable("batchId") Integer batchId){
        ServerResponse serverResponse = batchService.delBacth(batchId);
        return serverResponse;
    }

    /**
     * 自动批改
     */
    @ResponseBody
    public ServerResponse autoCheck(ExamStudent examStudent, Paper paper, List<Question> questions){
        ServerResponse serverResponse = examService.autoCheck(examStudent, paper, questions);
        return serverResponse;
    }
    /**
     * 删除考试
     */
    @DeleteMapping("/{exam_id}")
    @ResponseBody
    public ServerResponse deleteExam(@PathVariable("exam_id")Integer exam_id){
        return examService.deleteExam(exam_id);
    }

    /**
     * 通过批次id列出批次所有学生考试信息
     */
    @ResponseBody
    @GetMapping("/batch/details")
    public ServerResponse getBatchDetails(Integer batchId) {
        return batchService.getBatchDetails(batchId);
    }

}
