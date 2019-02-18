package com.explore.controller;

import com.explore.common.Const;
import com.explore.common.ResponseCode;
import com.explore.common.ServerResponse;
import com.explore.pojo.Batch;
import com.explore.pojo.Exam;
import com.explore.pojo.Student;
import com.explore.service.IBatchService;
import com.explore.service.IExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    IExamService examService;
    @Autowired
    IBatchService batchService;

    /**
     * 获取所有考试
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model){
        List<Exam> exams = examService.getExams();
        model.addAttribute("exams",exams);
        return "exam/list";
    }

    /**
     * 通过考试id列出所有批次
     * @param exam_id
     * @return
     */
    @RequestMapping("/batch/{exam_id}")
    public String batch(@PathVariable("exam_id") Integer exam_id,Model model){
        List<Batch> batches = batchService.getBatchesByExamId(exam_id);
        model.addAttribute("batches",batches);
        return "exam/batches";
    }

    /**
     * 考试批次报名
     * @return
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
}
