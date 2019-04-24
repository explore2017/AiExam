package com.explore.controller.student;

import com.explore.common.Const;
import com.explore.common.ResponseCode;
import com.explore.common.ServerResponse;
import com.explore.form.PasswordForm;
import com.explore.form.StudentInfoForm;
import com.explore.pojo.*;
import com.explore.pojo.Class;
import com.explore.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    IStudentService studentService;
    @Autowired
    IExamService examService;
    @Autowired
    IExamStudentService examStudentService;
    @Autowired
    IClassService classService;
    @Autowired
    IPaperService paperService;
    @Autowired
    IBatchService batchService;
    @Autowired
    IBatchStudentService batchStudentService;

    /**
     * 学生登录
     */
    @PostMapping("/login")
    public ServerResponse<Student> login(@RequestBody User user, HttpSession session) {
        ServerResponse<Student> serverResponse = studentService.login(user.getUsername(), user.getPassword());
        if (serverResponse.isSuccess()) {
            Student student = serverResponse.getData();
            session.setAttribute(Const.CURRENT_USER,student);
        }
        return serverResponse;
    }

    /**
     * 学生密码修改
     */
    @PostMapping("/password")
    public ServerResponse revise(HttpSession session, @RequestBody PasswordForm passwordForm) {
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        return studentService.revise(student.getSno(), passwordForm.getOldPassword(), passwordForm.getNewPassword());
    }

    /**
     * 重置密码
     */
    @PutMapping("/resetPassword")
    public ServerResponse resetPassword(String sno,String phone,String password){
        return studentService.resetPassword(sno,phone,password);
    }

    /**
     * 学生信息修改
     */
    @PostMapping("/reviseMessage")
    public ServerResponse response(HttpSession session,@RequestBody StudentInfoForm form){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if(student==null) {
            return ServerResponse.needLogin();
        }
        return studentService.reviseMessage(student.getId(),form.getPhone(),form.getEmail());
    }


    /**
     * 获取已参加考试
     */
    @GetMapping("/exam/batch")
    public ServerResponse<List<ExamStudent>>  examBatch(HttpSession session) {
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.createByErrorMessage("信息错误");
        }
//        List<ExamBatchVo> examBatchVoList = examService.getExamBatchVoByStudentId(student.getId());
        return examStudentService.getStudentExam(student.getId());
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/info")
    public ServerResponse<Student> info(HttpSession session) {
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        return ServerResponse.createBySuccess(student);
    }


    /**
     * 获取个人所属班级
     */
    @PostMapping("/myClass")
    public ServerResponse<List<Class>> getMyClass(HttpSession session) {
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        return classService.getClassesByStudentID(student.getId());
    }

    /**
     * 获取考试、批次列表
     */
    @GetMapping("/exam")
    public ServerResponse getExams(HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        return studentService.getExamVOs(student.getId());
    }

    @GetMapping("/batch/me")
    public ServerResponse myEnrollBatch(HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        return studentService.getMyEnrollBatch(student.getId());
    }


    @PostMapping("/batch/enroll")
    public ServerResponse batchEnroll(@RequestBody Batch batch, HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        return studentService.batchEnroll(batch.getId(),student.getId());
    }

    @PostMapping("/batch/cancel")
    public ServerResponse batchCancel(@RequestBody Batch batch, HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        return studentService.batchCancel(batch.getId(),student.getId());
    }

    @GetMapping("/batch/{id}/sign")
    @ApiOperation("用户签到")
    public ServerResponse signIn(@PathVariable("id") Integer batchId,HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        return batchStudentService.signIn(student.getId(),batchId);
    }

    @GetMapping("/batch/{id}/start")
    @ApiOperation("开始考试")
    public ServerResponse startExam(@PathVariable("id") Integer batchId,HttpSession session){
        Student student = (Student) session.getAttribute(Const.CURRENT_USER);
        if (student == null) {
            return ServerResponse.needLogin();
        }
        //开始答题
        ServerResponse serverResponse2 = examService.startReply(student.getId(),batchId);
        //验证
        ServerResponse serverResponse = checkCanStart(student.getId(),batchId);
        if (serverResponse.isSuccess()){
            BatchStudent batchStudent = batchStudentService.getByBatchIdAndStudentId(student.getId(),batchId);
            //开始考试
            if (batchStudent.getStatus()==Const.BATCH_STUDENT_STATUS.HAD_SIGN.getStatus()){
                //更新状态 1 > 2
                batchStudentService.updateReplyStart(student.getId(),batchId);
                //TODO 获取试卷然后记录返回
                Integer paperId = batchService.getPaperIdByBatchId(batchId);
                return paperService.getDetailsByPaperId(paperId);
            }else if (batchStudent.getStatus()==Const.BATCH_STUDENT_STATUS.IN_PROGRESS.getStatus()){
                //继续考试
                //TODO 从Record取
                Integer paperId = batchService.getPaperIdByBatchId(batchId);
                return paperService.getDetailsByPaperId(paperId);
            }else{
                return ServerResponse.createByError();
            }

        }else{
            return serverResponse;
        }
    }


    private ServerResponse checkCanStart(Integer studentId,Integer batchId){
        boolean flag = batchStudentService.checkCanStart(studentId,batchId);
        if (!flag){
            return ServerResponse.createByErrorMessage("考试已结束");
        }
        return ServerResponse.createBySuccess();
    }
}