package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.QuestionMapper;
import com.explore.dao.SubjectMapper;
import com.explore.pojo.Question;
import com.explore.pojo.Subject;
import com.explore.service.IQuestionService;
import com.explore.utils.ReadExcel;
import com.explore.utils.UploadImg;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Transactional
@Service
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    SubjectMapper subjectMapper;

    @Override
    public ServerResponse saveQuestion(Question question) {
        Date date = new Date();
        question.setCreateTime(date);
        question.setUpdateTime(date);
        questionMapper.insert(question);
        return ServerResponse.createBySuccessMessage("添加成功");
    }

    @Override
    public ServerResponse editQuestionByQuestionId(Integer questionId,Question newQuestion) {
      Question question= questionMapper.selectQuestionByQuestionId(questionId);
      if(question==null){return ServerResponse.createByErrorMessage("找不到这个题目");}
      Date date = new Date();
      newQuestion.setUpdateTime(date);
      int count=questionMapper.updateByPrimaryKey(newQuestion);
      if(count==0){return ServerResponse.createByErrorMessage("修改失败");}
        return ServerResponse.createBySuccess("修改成功");
    }

    @Override
    public ServerResponse deleteQuestionByQuestionId(Integer questionId) {
        Question question= questionMapper.selectQuestionByQuestionId(questionId);
        if(question==null){return ServerResponse.createByErrorMessage("找不到这个题目");}
        try{
            questionMapper.deleteByPrimaryKey(questionId);
        }catch (Exception e){
            return ServerResponse.createByErrorMessage("该题目有被引用，无法删除改题目");
        }
        return ServerResponse.createBySuccessMessage("删除题目成功");
    }

    @Override
    public ServerResponse<Question> getQuestionsByQuestionId(Integer questionId) {
        Question question= questionMapper.selectQuestionByQuestionId(questionId);
        if(question==null){return ServerResponse.createByErrorMessage("找不到这个题目");}
        return ServerResponse.createBySuccess(question);
    }

    @Override
    public ServerResponse<List<Question>> getAllQuestions() {
        return ServerResponse.createBySuccess(questionMapper.selectAllQuestions());
    }

    @Override
    public ServerResponse<List<Question>> getQuestionsByCondition(Integer subjectId, Integer difficulty, Integer questionTypeId, String keyPoint){
        if(difficulty==-1){
            difficulty=null;
        }
        if(questionTypeId==-1){
            questionTypeId=null;
        }
        if(keyPoint!=null&&keyPoint.equals("")){
            keyPoint=null;
        }
        List<Question> questionList=questionMapper.selectQuestionsByCondition(subjectId,difficulty,questionTypeId,keyPoint);
        return ServerResponse.createBySuccess(questionList);
    }

    @Override
    public ServerResponse batchImport(MultipartFile file,Integer subjectId) {
        List<Map<Integer,String>> allData= ReadExcel.readExcelContentByList(file);
        Question question=new Question();
        int time=0;
        for(Map<Integer,String> data:allData ){
            Integer type=questionTypeId(data.get(0));
            question.setTitle(data.get(0));
            question.setContent(data.get(1));
            question.setQuestionTypeId(type);
            question.setAnswer(data.get(2));
            question.setDifficulty(difficulty(data.get(3)));
            question.setSubjectId(subjectId);
            question.setKeyPoint(data.get(4));
            if(type==0||type==2){
                String selects=data.get(5);
                for(int i=6;i<9;i++){
                    selects+="&&&"+data.get(i);
                }
                question.setSelects(selects);
            }
            if(type<3){
                question.setIsSubjective(0);
            }else{
                question.setIsSubjective(1);
            }
            question.setCreateTime(new Date());
            question.setUpdateTime(new Date());
            int count= questionMapper.insert(question);
            if(count>0){
                time++;
            }
        }
        if(time>0){
            return ServerResponse.createBySuccessMessage("批量导入题目,成功插入题目"+time+"条");
        }
        return  ServerResponse.createByErrorMessage("批量导入题目失败");
    }

    @Override
    public ServerResponse uploadImg(MultipartFile file, HttpServletRequest request) throws FileNotFoundException {
        File targetFile=null;
        String url="";//返回存储路径
        String fileName=file.getOriginalFilename();//获取文件名加后缀
        if(fileName!=null&&fileName.equals("")){
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() +"/static/upload/imgs/";//存储路径
            String path = request.getSession().getServletContext().getRealPath("/static/upload/imgs"); //文件存储位置
            String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());//文件后缀
            fileName=new Date().getTime()+"_"+new Random().nextInt(1000)+fileF;//新的文件名

            //先判断文件是否存在
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fileAdd = sdf.format(new Date());
            //获取文件夹路径
            File file1 =new File(path+"/"+fileAdd);
            //如果文件夹不存在则创建
            if(!file1 .exists()  && !file1 .isDirectory()){
                file1 .mkdirs();
            }
            //将图片存入文件夹
            targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);
                url=returnUrl+fileAdd+"/"+fileName;
                return  ServerResponse.createBySuccessMessage("上传成功",url);
            } catch (Exception e) {
                e.printStackTrace();
                return  ServerResponse.createByErrorMessage("上传失败");
            }
        }
        return  ServerResponse.createByErrorMessage("上传失败");
    }

    @Override
    public void getFile(HttpServletResponse response) {
        File file=new File("F:\\工作2\\AiExam\\src\\main\\resources\\static\\批量导入试题样本.xlsx");
        try {
            FileInputStream in = new FileInputStream(file);
            UploadImg.setResponseHeader(response,"批量导入试题样本.xlsx");
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            //循环取出流中的数据
            while((len = in.read(buffer)) != -1){
                os.write(buffer,0,len);
            }
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer questionTypeId(String title){
        if(title==null) {return null;}
        if(title.equals("选择题")){
            return  0;
        }
        if(title.equals("判断题")){
            return  1;
        }
        if(title.equals("多选题")){
            return  2;
        }
        if(title.equals("填空题")){
            return  3;
        }
        if(title.equals("简答题")){
            return  4;
        }
        if(title.equals("分析题")){
            return  5;
        }
        return  null;
    }

    public  Integer difficulty(String diff){
        if(diff==null) {return 0;}
        if(diff.equals("简单")){
            return  0;
        }
        if(diff.equals("中等")){
            return  1;
        }
        if(diff.equals("困难")){
            return  2;
        }
        return  0;
    }

    public  Integer subjectId(String subjectName){
     Subject subject=subjectMapper.getOneSuject(subjectName);
     if(subject!=null){
         return subject.getId();
     }
        return null;
    }

}
