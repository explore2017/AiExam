package com.explore.schedule;

import com.explore.common.Const;
import com.explore.dao.BatchMapper;
import com.explore.dao.BatchStudentMapper;
import com.explore.pojo.Batch;
import com.explore.pojo.BatchStudent;
import com.explore.service.IExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author PinTeh
 * @date 2019/5/7
 */
@Component
@Slf4j
public class MonitorSchedule {

    @Autowired
    private BatchMapper batchMapper;
    @Autowired
    private BatchStudentMapper batchStudentMapper;
    @Autowired
    private IExamService examService;
    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void updateStatus(){
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        log.info("定时任务开始执行,执行时间:{}",simpleDateFormat.format(now));

        List<Batch> batches = batchMapper.selectBatchesTimeOut(simpleDateFormat.format(now));

        log.info("一共查询出{}条未处理的批次，即将开始处理...",batches.size());
        //查询未更新的批次
        for (Batch batch : batches) {
            //查询该批次的学生批次表
            List<BatchStudent> batchStudents = batchStudentMapper.selectByBatchId(batch.getId());
            log.info("开始处理该id为{}批次 {}个学生考试状态更新",batch.getId(),batchStudents.size());
            for (BatchStudent batchStudent : batchStudents) {
                int currentStatus = batchStudent.getStatus();
                //未开始考试 0-未签到  1-签到
                if (currentStatus==Const.BATCH_STUDENT_STATUS.NOT_SIGN.getStatus()||currentStatus==Const.BATCH_STUDENT_STATUS.HAD_SIGN.getStatus()){
                    //设置为缺考
                    log.info("学生{}被记为缺考,上一状态{}",batchStudent.getStudentId(),currentStatus);
                    BatchStudent newBatchStudent = new BatchStudent();
                    newBatchStudent.setId(batchStudent.getId());
                    newBatchStudent.setStatus(Const.BATCH_STUDENT_STATUS.MISSING_TEST.getStatus());
                    newBatchStudent.setScore(0.0);
                    batchStudentMapper.updateByPrimaryKeySelective(newBatchStudent);
                }else if(currentStatus==Const.BATCH_STUDENT_STATUS.IN_PROGRESS.getStatus()){
                    //正在考试状态  设置为考试完成 即强制提交
                    BatchStudent newBatchStudent = new BatchStudent();
                    newBatchStudent.setId(batchStudent.getId());
                    newBatchStudent.setStatus(Const.BATCH_STUDENT_STATUS.FINISHED.getStatus());
                    newBatchStudent.setSubmitTime(now);
                    newBatchStudent.setUpdateTime(now);
                    batchStudentMapper.updateByPrimaryKeySelective(newBatchStudent);
                    examService.autoJudge(batchStudent.getStudentId(),batch.getId());
                }
            }
            //使用describe 减少查询结果
            Batch newBatch = new Batch();
            newBatch.setId(batch.getId());
            newBatch.setDescribe(Const.BATCH_STUDENT_STATUS.FINISHED.getDesc());
            batchMapper.updateByPrimaryKeySelective(newBatch);
        }
        log.info("本次任务处理完成{}",simpleDateFormat.format(new Date()));
    }
}
