package com.explore.controller;

import com.explore.common.Const;
import com.explore.common.ResponseCode;
import com.explore.common.ServerResponse;
import com.explore.form.PaperRecordForm;
import com.explore.pojo.*;
import com.explore.service.IBatchService;
import com.explore.service.IBatchStudentService;
import com.explore.service.IExamService;
import com.explore.service.IExamStudentService;
import com.explore.vo.PaperQuestionVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    IExamService examService;
    @Autowired
    IBatchService batchService;
    @Autowired
    IExamStudentService examStudentService;
    @Autowired
    IBatchStudentService batchStudentService;

    /**
     * 获取所有考试
     */
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse list(){
        return examService.getExams();
    }

    /**
     * 通过考试id列出所有批次
     */
    @GetMapping("/batch")
    public ServerResponse batch( Integer examId){
        return batchService.getBatchesByExamId(examId);
    }

    /**
     * 考试批次报名
     */
    @RequestMapping("/batch/enroll/{batch_id}")
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
    public ServerResponse deleteBatchStudent(@PathVariable("studentId")Integer studentId,Integer batchId){
        return batchService.deleteBatchStudent(studentId,batchId);
    }

    /**
     * 添加考试
     */
    @PostMapping
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
    public ServerResponse delBatch(@PathVariable("batchId") Integer batchId){
        ServerResponse serverResponse = batchService.delBacth(batchId);
        return serverResponse;
    }

    /**
     * 自动批改
     */
    public ServerResponse autoCheck(ExamStudent examStudent, Paper paper, List<Question> questions){
        ServerResponse serverResponse = examService.autoCheck(examStudent, paper, questions);
        return serverResponse;
    }
    /**
     * 删除考试
     */
    @DeleteMapping("/{exam_id}")
    public ServerResponse deleteExam(@PathVariable("exam_id")Integer exam_id){
        return examService.deleteExam(exam_id);
    }

    /**
     * 通过批次id列出批次所有学生考试信息
     */
    @GetMapping("/batch/details")
    public ServerResponse getBatchDetails(Integer batchId) {
        return batchService.getBatchDetails(batchId);
    }

    @PostMapping("/batch/{id}/sign")
    @ApiOperation("用户签到")
    public ServerResponse signIn(@PathVariable("id") Integer batchId,HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        return batchStudentService.signIn(student.getId(),batchId);
    }

    @GetMapping("/batch/{id}/check")
    @ApiOperation("点击开始考试")
    public ServerResponse checkExam(@PathVariable("id") Integer batchId,HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        // 1、batch_student 有记录
        // 2、 status == (1,2)
        // 3、是否在考试时间内
        return checkCanStart(student.getId(),batchId);
    }

    @PostMapping("/batch/{id}/start")
    @ApiOperation("开始考试")
    public ServerResponse startExam(@PathVariable("id") Integer batchId,HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        ServerResponse serverResponse =checkCanStart(student.getId(),batchId);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        return examService.startReply(student.getId(),batchId);
    }

    @PostMapping("/batch/{id}/monitor")
    @ApiOperation("监听器")
    public ServerResponse monitor(@PathVariable("id") Integer batchId, @RequestBody PaperRecordForm model, HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        return examService.monitor(student.getId(),batchId,model.getRecords(),false);
    }

    @PostMapping("/batch/{id}/submit")
    public ServerResponse submit(@PathVariable("id") Integer batchId, @RequestBody PaperRecordForm model, HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        return examService.monitor(student.getId(),batchId,model.getRecords(),true);
    }
    @GetMapping("/read_paper/{id}")
    @ApiOperation("阅卷")
    public ServerResponse readPaper(@PathVariable("id") Integer batchStudentId,HttpSession session){
//        Teacher student = (Teacher) session.getAttribute(Const.CURRENT_USER);
//        if (student == null) {
//            return ServerResponse.needLogin();
//        }

        return examService.readPaper(batchStudentId);
    }
    @PostMapping("/read_paper/{id}")
    @ApiOperation("阅卷")
    public ServerResponse readPaperSubmit(@PathVariable("id") Integer batchStudentId,@RequestBody PaperRecordForm model,HttpSession session){
//        Teacher student = (Teacher) session.getAttribute(Const.CURRENT_USER);
//        if (student == null) {
//            return ServerResponse.needLogin();
//        }
        return examService.readPaperSubmit(batchStudentId,model.getRecords());
    }

    private ServerResponse checkCanStart(Integer studentId,Integer batchId){
        return batchStudentService.checkCanStart(studentId,batchId);
    }
}
