package com.explore.service.Impl;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.dao.*;
import com.explore.pojo.*;
import com.explore.pojo.Class;
import com.explore.service.IBatchStudentService;
import com.explore.service.IExamService;
import com.explore.vo.ExamBatchVo;
import com.explore.vo.PaperComposeVo;
import com.explore.vo.PaperQuestionVo;
import com.explore.vo.QuestionVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class ExamServiceImpl implements IExamService {

    @Autowired
    ExamMapper examMapper;
    @Autowired
    BatchMapper batchMapper;
    @Autowired
    BatchStudentMapper batchStudentMapper;
    @Autowired
    PaperServiceImpl paperService;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    ClassMapper classMapper;
    @Autowired
    SubjectMapper subjectMapper;
    @Autowired
    IBatchStudentService batchStudentService;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PaperRecordMapper paperRecordMapper;

    @Override
    public ServerResponse getExams() {
        List<Exam> examList=examMapper.selectExams();
        List<HashMap<String,Object>>  allData=new ArrayList<>();
        for(Exam exam:examList){
            Class class1=classMapper.selectByPrimaryKey(exam.getClassId());
            if(class1==null){return  ServerResponse.createByError();}
            Teacher teacher=teacherMapper.selectByPrimaryKey(class1.getTeacherId());
            if(teacher==null){return  ServerResponse.createByError();}
            teacher.setPassword(StringUtils.EMPTY);
            teacher.setUsername(StringUtils.EMPTY);
            Subject subject=subjectMapper.selectByPrimaryKey(class1.getSubjectId());
            HashMap<String,Object> data=new HashMap<>();
            data.put("exam",exam);
            data.put("class",class1);
            data.put("teacher",teacher);
            data.put("subject",subject);
            allData.add(data);
        }
        return ServerResponse.createBySuccess(allData) ;
    }

    @Override
    public List<ExamBatchVo> getExamBatchVoByStudentId(Integer student_id) {
        List<ExamBatchVo> exams = examMapper.selectExamBatchVoByStudentId(student_id);
        return exams;
    }

    @Override
    public ServerResponse save(Exam exam) {
        exam.setCreateTime(new Date());
        exam.setUpdateTime(new Date());
        if(examMapper.insert(exam)>0){
            JSONArray batchArray=JSONArray.fromObject(exam.getSubscribe());
            for(int i=0;i<batchArray.size();i++){
                JSONObject jsonObject=batchArray.getJSONObject(i);
                Batch batch=new Batch();
                batch.setName(exam.getName()+"考试的批次"+(i+1));
                batch.setExamId(exam.getId());
                batch.setMaxNumber(jsonObject.getInt("maxNumber"));
                batch.setPaperId(exam.getPaperId());
                try{
                    batch.setStartTime(  new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(jsonObject.getString("startTime")));
                    batch.setEndTime(  new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(jsonObject.getString("endTime")));
                } catch (ParseException e){
                    return  ServerResponse.createByErrorMessage("创建考试失败");
                }
                if(!(batchMapper.insert(batch)>0)){
                    return  ServerResponse.createByErrorMessage("创建考试失败");
                }
            }
        }
        return ServerResponse.createBySuccessMessage("创建考试成功");
    }

    @Override
    public ServerResponse autoCheck(ExamStudent examStudent, Paper paper, List<Question> questions){
        Double score = 0.0;
        ServerResponse<List<Question>> answer =  paperService.getDetailsByPaperId(paper.getId());
        for(int i = 0;i<questions.size();i++){
            if(checkJudge(questions.get(i)) && questions.get(i).getAnswer().equals(answer.getData().get(i).getAnswer()))
                score += Double.valueOf(questions.get(i).getDefaultScore());
        }
        examStudent.setScore(score);
        return ServerResponse.createBySuccess(examStudent);
    }

    @Override
    public ServerResponse deleteExam(Integer examId) {
        Exam exam=examMapper.selectByPrimaryKey(examId);
        if(exam==null){return  ServerResponse.createByErrorMessage("没有这个考试");}
        List<Batch> batches=batchMapper.selectBatchesByExamId(examId);
        if(batches==null){
            examMapper.deleteByPrimaryKey(examId);
            return ServerResponse.createBySuccessMessage("删除考试成功");
        }else {
            for(Batch batch:batches){
             batchStudentMapper.deleteByBatchId(batch.getId());
             batchMapper.deleteByPrimaryKey(batch.getId());
            }
            examMapper.deleteByPrimaryKey(examId);
            return ServerResponse.createBySuccessMessage("删除考试成功");
        }
    }

    @Override
    public ServerResponse startReply(Integer studentId, Integer batchId) {
        List<PaperQuestionVo> paperQuestionVos;

        Batch batch = batchMapper.selectByPrimaryKey(batchId);
        long countDown = batch.getEndTime().getTime() - System.currentTimeMillis();

        BatchStudent batchStudent = batchStudentMapper.selectByStudentIdAndBatchId(studentId,batchId);
        int status = batchStudent.getStatus();
        //开始考试
        if (status == Const.BATCH_STUDENT_STATUS.HAD_SIGN.getStatus()){
            //更新状态 1 > 2
            BatchStudent bs = new BatchStudent();
            bs.setId(batchStudent.getId());
            bs.setStartTime(new Date());
            bs.setStatus(Const.BATCH_STUDENT_STATUS.IN_PROGRESS.getStatus());
            batchStudentMapper.updateByPrimaryKeySelective(bs);
            //获取试卷
            Integer paperId = batchMapper.selectPaperIdByBatchId(batchId);
            //获取后封装成PaperQuestionVo返回
            ServerResponse<List<PaperComposeVo>> serverResponse = paperService.getDetailsByPaperId(paperId);
            //插入
            List<PaperComposeVo> paperComposeVos = serverResponse.getData();
            List<PaperRecord> paperRecords = new ArrayList<>();
            for (PaperComposeVo paperComposeVo : paperComposeVos) {
                PaperRecord paperRecord = new PaperRecord();
                paperRecord.setBatchId(batchId);
                paperRecord.setStudentId(studentId);
                paperRecord.setSequence(paperComposeVo.getSequence());
                paperRecord.setQuestionId(paperComposeVo.getQuestionId());
                paperRecord.setSingleScore(paperComposeVo.getSingleScore());
                paperRecords.add(paperRecord);
            }
            try {
                paperRecordMapper.insertRecords(paperRecords);
            }catch (Exception e){
                throw new RuntimeException("插入记录异常");
            }
            paperQuestionVos = packagePaperRecordToPaperQuestionVo(paperRecords,false);
            //简单开启个定时任务 更新状态即可
            /*
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    BatchStudent bs = new BatchStudent();
                    bs.setId(batchStudent.getId());
                    bs.setStatus(Const.BATCH_STUDENT_STATUS.FINISHED.getStatus());
                    batchStudentMapper.updateByPrimaryKeySelective(bs);
                }
            },countDown,TimeUnit.MILLISECONDS);
            */

        }else if (status == Const.BATCH_STUDENT_STATUS.IN_PROGRESS.getStatus()){
            //从record表中获取
            List<PaperRecord> paperRecords = paperRecordMapper.selectByStudentIdAndBatchId(studentId,batchId);
            //获取后封装成PaperQuestionVo返回
            paperQuestionVos = packagePaperRecordToPaperQuestionVo(paperRecords,false);
        }else{
            return ServerResponse.createByError();
        }
        //封装返回对象
        Map<String,Object> map = new HashMap<>(2);
        map.put("data",paperQuestionVos);
        map.put("countDown",countDown-1000*5);
        return ServerResponse.createBySuccess(map);
    }

    @Override
    public ServerResponse monitor(Integer studentId, Integer batchId, List<PaperQuestionVo> records,Boolean isSubmit) {
        boolean canStart = batchStudentService.checkCanStart(studentId,batchId);
        if (!canStart){
            //返回成功
            return ServerResponse.createBySuccessMessage("考试已结束");
        }
        Batch batch = batchMapper.selectByPrimaryKey(batchId);
        Date now = new Date();
        int flag = batch.getEndTime().compareTo(now);
        if (flag>=0){
            //未到时间
            paperRecordMapper.updateRecords(studentId,batchId,records);
            //提交更新状态
            if (isSubmit){
                BatchStudent batchStudent = batchStudentMapper.selectByStudentIdAndBatchId(studentId,batchId);
                batchStudent.setStatus(Const.BATCH_STUDENT_STATUS.FINISHED.getStatus());
                batchStudent.setSubmitTime(now);
                batchStudentMapper.updateByPrimaryKeySelective(batchStudent);
                return ServerResponse.createBySuccessMessage("提交考试成功");
            }
        }else{
            //时间到
            BatchStudent batchStudent = batchStudentMapper.selectByStudentIdAndBatchId(studentId,batchId);
            if(batchStudent.getStatus()==Const.BATCH_STUDENT_STATUS.IN_PROGRESS.getStatus()){
                batchStudent.setStatus(Const.BATCH_STUDENT_STATUS.FINISHED.getStatus());
                batchStudentMapper.updateByPrimaryKeySelective(batchStudent);
            }
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse readPaper(Integer batchStudentId) {
        BatchStudent batchStudent=batchStudentMapper.selectByPrimaryKey(batchStudentId);
        if(batchStudent==null){
            return ServerResponse.createByErrorMessage("没有这个学生的考试");
        }
        if(batchStudent.getStatus()!=Const.BATCH_STUDENT_STATUS.FINISHED.getStatus()&&batchStudent.getStatus()!=Const.BATCH_STUDENT_STATUS.GET_SCORE.getStatus()){
            return ServerResponse.createByErrorMessage("还未可以阅卷");
        }
        List<PaperRecord> paperRecords=paperRecordMapper.selectByStudentIdAndBatchId(batchStudent.getStudentId(),batchStudent.getBatchId());
        List<PaperQuestionVo> paperQuestionVos = packagePaperRecordToPaperQuestionVo(paperRecords,true);
        Map<String,Object> map = new HashMap<>(1);
        map.put("data",paperQuestionVos);
        return ServerResponse.createBySuccess(map);
    }

    @Override
    public ServerResponse readPaperSubmit(Integer batchStudentId, List<PaperQuestionVo> records) {
        BatchStudent batchStudent=batchStudentMapper.selectByPrimaryKey(batchStudentId);
        if(batchStudent==null){
            return ServerResponse.createByErrorMessage("没有这个学生的考试");
        }
        if(batchStudent.getStatus()!=Const.BATCH_STUDENT_STATUS.FINISHED.getStatus()&&batchStudent.getStatus()!=Const.BATCH_STUDENT_STATUS.GET_SCORE.getStatus()){
            return ServerResponse.createByErrorMessage("还未可以阅卷");
        }
        if(paperRecordMapper.updateScore(batchStudent.getStudentId(),batchStudent.getBatchId(),records)==1){
            Double totalScore=paperRecordMapper.getTotalScore(batchStudent.getStudentId(),batchStudent.getBatchId());
            batchStudent.setScore(totalScore);
            batchStudent.setStatus(5);
            batchStudent.setUpdateTime(new Date());
            if(batchStudentMapper.updateByPrimaryKeySelective(batchStudent)==1){
                return ServerResponse.createBySuccessMessage("提交成功");
            }
        }
        return ServerResponse.createByErrorMessage("提交失败");
    }

    private List<PaperQuestionVo> packagePaperRecordToPaperQuestionVo(List<PaperRecord> paperRecords,Boolean answer){
        List<PaperQuestionVo> paperQuestionVos = new ArrayList<>();
        for (PaperRecord paperRecord : paperRecords) {
            PaperQuestionVo paperQuestionVo = new PaperQuestionVo();
            //封装QuestionVo对象
            Question question = questionMapper.selectByPrimaryKey(paperRecord.getQuestionId());
            if(!answer){
                question.setAnswer("");
            }
            QuestionVo questionVo = modelMapper.map(question,QuestionVo.class);
            paperQuestionVo.setQuestion(questionVo);
            paperQuestionVo.setReply(paperRecord.getReply());
            paperQuestionVo.setSequence(paperRecord.getSequence());
            paperQuestionVo.setSingleScore(paperRecord.getSingleScore());
            if(answer){
                paperQuestionVo.setScore(paperRecord.getScore());
            }
            paperQuestionVos.add(paperQuestionVo);

        }
        return paperQuestionVos;
    }

    /**
     * 判断问题是否需要自动批改
     */
    public boolean checkJudge(Question question){
        if(question.getQuestionTypeId() == 3 || question.getQuestionTypeId() == 5 || question.getQuestionTypeId() == 6){
            return false;
        }
        return true;
    }
}
