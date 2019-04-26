package com.explore.thread;

import com.explore.common.Const;
import com.explore.dao.BatchStudentMapper;
import com.explore.dao.PaperMapper;
import com.explore.dao.PaperRecordMapper;
import com.explore.dao.QuestionMapper;
import com.explore.pojo.BatchStudent;
import com.explore.pojo.PaperRecord;
import com.explore.pojo.Question;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author PinTeh
 * @date 2019/4/26
 */
public class AutoCheck implements Runnable {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    PaperRecordMapper paperRecordMapper;
    @Autowired
    BatchStudentMapper batchStudentMapper;

    private final Integer studentId;

    private final Integer batchId;

    public AutoCheck(Integer studentId, Integer batchId) {
        this.studentId = studentId;
        this.batchId = batchId;
    }

    @Override
    public void run() {
        //判断是否能全部自动阅卷
        boolean canAutoCheck = true;
        List<PaperRecord> paperRecords = paperRecordMapper.selectByStudentIdAndBatchId(studentId,batchId);
        for (PaperRecord paperRecord : paperRecords) {
            Question question = questionMapper.selectByPrimaryKey(paperRecord.getQuestionId());
            if (!checkJudge(question)){
                //非客观题
                canAutoCheck = false;
            }else{
                //客观题
                String reply = (paperRecord.getReply()).trim();
                if (question.getQuestionTypeId()==0||question.getQuestionTypeId()==1){
                    //0、单选 1、判断
                    if(reply.equals((question.getAnswer()).trim())){
                        paperRecord.setScore(paperRecord.getSingleScore());
                    }else{
                        paperRecord.setScore(0.0);
                    }
                }else if(question.getQuestionTypeId()==2){
                    //2、多选 可能顺序不一样，因此需要遍历判断
                    //计分规则：漏选得一般、错选多选0分
                    String[] answers = (question.getAnswer()).trim().split(",");
                    String[] res = (paperRecord.getReply()).trim().split(",");
                    if (answers.length>res.length){
                        //漏选
                        int right = 0;
                        for (String r : res) {
                            for (String answer : answers) {
                                if (r.equals(answer)){
                                    right++;
                                }
                            }
                        }
                        if (right!=res.length){
                            paperRecord.setScore(0.0);
                        }else{
                            paperRecord.setScore(paperRecord.getSingleScore()/2);
                        }
                    }else if (answers.length==res.length){
                        int right = 0;
                        for (String r : res) {
                            for (String answer : answers) {
                                if (r.equals(answer)){
                                    right++;
                                }
                            }
                        }
                        if (right!=res.length){
                            paperRecord.setScore(0.0);
                        }else{
                            paperRecord.setScore(paperRecord.getSingleScore());
                        }
                    }else{
                        //多选0分
                        paperRecord.setScore(0.0);
                    }
                }
            }
        }
        if (canAutoCheck){
            //设置总分数
            List<PaperRecord> list = paperRecordMapper.selectByStudentIdAndBatchId(studentId,batchId);
            Double totalScore = 0.0;
            for (PaperRecord paperRecord : list) {
                totalScore+=paperRecord.getScore();
                BatchStudent batchStudent = batchStudentMapper.selectByStudentIdAndBatchId(studentId,batchId);
                batchStudent.setScore(totalScore);
                batchStudent.setUpdateTime(new Date());
                batchStudent.setStatus(Const.BATCH_STUDENT_STATUS.GET_SCORE.getStatus());
                batchStudentMapper.updateByPrimaryKeySelective(batchStudent);
            }

        }
    }

    private boolean checkJudge(Question question){
        int type = question.getQuestionTypeId();
        if(type == 3 || type == 5 || type == 4){
            return false;
        }
        return true;
    }
}
