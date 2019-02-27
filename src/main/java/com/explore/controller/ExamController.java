package com.explore.controller;

import com.explore.common.Const;
import com.explore.common.ResponseCode;
import com.explore.common.ServerResponse;
import com.explore.pojo.Batch;
import com.explore.pojo.Exam;
import com.explore.pojo.Student;
import com.explore.pojo.Teacher;
import com.explore.service.IBatchService;
import com.explore.service.IExamService;
import com.explore.service.IExamStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

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
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model){
        List<Exam> exams = examService.getExams();
        model.addAttribute("exams",exams);
        return "exam/list";
    }

    /**
     * 通过考试id列出所有批次
     */
    @RequestMapping("/batch/{exam_id}")
    public String batch(@PathVariable("exam_id") Integer exam_id,Model model){
        List<Batch> batches = batchService.getBatchesByExamId(exam_id);
        model.addAttribute("batches",batches);
        return "exam/batches";
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
    public ServerResponse add(Exam exam){
        ServerResponse serverResponse = examService.save(exam);
        return serverResponse;
    }


    /**
     * 添加考试批次
     */
    @PostMapping("batch")
    @ResponseBody
    public ServerResponse addBatch(Batch batch){
        ServerResponse serverResponse = batchService.addBacth(batch);
        return serverResponse;
    }

    /**
     * 考试信息发布
     */
    @PostMapping("/push")
    public String push(Model model) {
        List<Exam> exams = examService.getExams();
        model.addAttribute("exams",exams);
        return "exam/list";
    }
}
