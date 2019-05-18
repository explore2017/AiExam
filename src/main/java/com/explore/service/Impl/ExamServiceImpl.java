package com.explore.service.Impl;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.dao.*;
import com.explore.pojo.*;
import com.explore.pojo.Class;
import com.explore.service.IBatchStudentService;
import com.explore.service.IExamService;
import com.explore.thread.AutoCheck;
import com.explore.utils.ExcelUtil;
import com.explore.vo.ExamBatchVo;
import com.explore.vo.PaperComposeVo;
import com.explore.vo.PaperQuestionVo;
import com.explore.vo.QuestionVo;
import io.swagger.models.auth.In;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
    @Autowired
    StudentMapper studentMapper;

    @Override
    public ServerResponse getExams(String role,Integer teacherId) {
        List<Exam> examList;
        if(role.equals(Const.Manager)){
            examList =examMapper.selectExams();
        }else {
            examList =examMapper.selectTeacherExams(teacherId);
        }
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
        Paper paper=paperService.getPaperById(exam.getPaperId()).getData();
        if(paper==null){
            return  ServerResponse.createByErrorMessage("添加考试失败");
        }
        exam.setPassScore(paper.getPassScore()*0.6);
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
    public ServerResponse change(Exam exam) {
        examMapper.updateByPrimaryKeySelective(exam);
        return ServerResponse.createBySuccessMessage("设置成功");
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
                    //提交后可以终止、若不终止则需判断是否需要更新
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
        ServerResponse serverResponse = batchStudentService.checkCanStart(studentId,batchId);
        if (!serverResponse.isSuccess()){
            //返回成功
            return ServerResponse.createBySuccessMessage(serverResponse.getMsg());
        }
        Batch batch = batchMapper.selectByPrimaryKey(batchId);
        Date now = new Date();
        int flag = batch.getEndTime().compareTo(now);
        //未到时间
        if (flag>=0){
            paperRecordMapper.updateRecords(studentId,batchId,records);
            //手动提交
            if (isSubmit){
                BatchStudent batchStudent = batchStudentMapper.selectByStudentIdAndBatchId(studentId,batchId);
                batchStudent.setStatus(Const.BATCH_STUDENT_STATUS.FINISHED.getStatus());
                batchStudent.setSubmitTime(now);
                batchStudentMapper.updateByPrimaryKeySelective(batchStudent);
                autoJudge(studentId,batchId);
                return ServerResponse.createBySuccessMessage("提交考试成功");
            }
        }else{
            //时间到 将考试状态设置为已完成
            BatchStudent batchStudent = batchStudentMapper.selectByStudentIdAndBatchId(studentId,batchId);
            if(batchStudent.getStatus()==Const.BATCH_STUDENT_STATUS.IN_PROGRESS.getStatus()){
                BatchStudent newBatchStudent = new BatchStudent();
                newBatchStudent.setId(batchStudent.getId());
                newBatchStudent.setStatus(Const.BATCH_STUDENT_STATUS.FINISHED.getStatus());
                newBatchStudent.setSubmitTime(now);
                batchStudentMapper.updateByPrimaryKeySelective(newBatchStudent);
            }
            autoJudge(studentId,batchId);
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

    @Override
    public ServerResponse examScore(Integer examId) {
        Exam exam=examMapper.selectByPrimaryKey(examId);
        if(exam==null){
            return ServerResponse.createByErrorMessage("找不到该考试");
        }
        List<Map<String,Object>> allData=new ArrayList<>();
        List<BatchStudent>  batchStudentList=batchStudentMapper.selectExamScore(examId);
        for (BatchStudent batchStudent : batchStudentList) {
            Map<String,Object> map=new HashMap<>();
            Student student =studentMapper.selectByPrimaryKey(batchStudent.getStudentId());
            if(null!=student){
                map.put("sno",student.getSno());
                map.put("name",student.getName());
                map.put("exam",batchStudent);
                map.put("passScore",exam.getPassScore());
                allData.add(map);
            }
        }
        return ServerResponse.createBySuccess(allData);
    }

    @Override
    public void  exportScore(Integer examId,HttpServletResponse response) {
        Exam exam=examMapper.selectByPrimaryKey(examId);
        if(exam==null){
            return ;
        }
        Class class1=classMapper.selectByPrimaryKey(exam.getClassId());
        if(class1==null){
            return ;
        }
        List<Map<String,Object>> allData=(List<Map<String, Object>>) examScore(examId).getData();
        //excel标题
         String[] title = {"序号","学号","姓名","成绩","开始时间","交卷时间","是否及格"};
         //excel文件名
        String fileName = exam.getName()+"成绩报表"+".xls";
        //sheet名
         String sheetName = class1.getName()+"班级成绩报表";
         String[][] content=new String[allData.size()][7];
         Double passScore=exam.getPassScore();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
         for (int i = 0; i < allData.size(); i++) {
            Map<String,Object> map = allData.get(i);
            content[i][0] = i+1+"";
            content[i][1] = map.get("sno").toString();
            content[i][2] = map.get("name").toString();
            BatchStudent batchStudent =(BatchStudent)map.get("exam");
            if(batchStudent.getStatus()==4){
                content[i][3]="缺考";
                content[i][4]="缺考";
                content[i][5]="缺考";
                content[i][6]="缺考";
            }else{
                content[i][3]=batchStudent.getScore().toString();
                content[i][4]=sdf.format(batchStudent.getStartTime());
                content[i][5]=sdf.format(batchStudent.getSubmitTime());
                if(batchStudent.getScore()>=passScore){
                    content[i][6]="是";
                }else{
                    content[i][6]="否";
                }

            }
         }
         HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
           e.printStackTrace();
          }
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

    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 判断问题是否需要自动批改
     */
    public boolean checkJudge(Question question){
        int type = question.getQuestionTypeId();
        if(type == 3 || type == 5 || type == 4){
            return false;
        }
        return true;
    }

    /**
     * 自动阅卷
     */
    @Override
    public void autoJudge(Integer studentId,Integer batchId){
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
            paperRecordMapper.updateByPrimaryKeySelective(paperRecord);
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
}
