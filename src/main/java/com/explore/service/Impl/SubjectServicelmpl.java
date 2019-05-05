package com.explore.service.Impl;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.dao.QuestionMapper;
import com.explore.dao.SubjectMapper;
import com.explore.dao.TeacherSubjectMapper;
import com.explore.pojo.Subject;
import com.explore.pojo.TeacherSubject;
import com.explore.service.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubjectServicelmpl implements ISubjectService {

    @Autowired
    SubjectMapper subjectMapper;
    @Autowired
    TeacherSubjectMapper teacherSubjectMapper;
    @Autowired
    QuestionMapper questionMapper;

    @Override
    public ServerResponse<List<Subject>> Subject(String role,Integer teacherId) {
        if(role.equals(Const.Teacher)){
            return ServerResponse.createBySuccess(subjectMapper.getTeacherSubject(teacherId));
        }else{
            return ServerResponse.createBySuccess(subjectMapper.getAllSubject());
        }
    }

    @Override
    public ServerResponse addSubject(Subject subject) {
        if(subject==null){
            return ServerResponse.createByErrorMessage("不能为空");
        }
        Subject subject1=subjectMapper.getOneSuject(subject.getName());
        if(subject1!=null){
            return  ServerResponse.createByErrorMessage("科目名称已存在");
        }
        if(subjectMapper.insert(subject)==1) {
            return ServerResponse.createBySuccessMessage("增加成功");
        }
        Date creat_time = new Date();
        subject.setCreateTime(creat_time);
        return ServerResponse.createByErrorMessage("增加失败");
    }

    @Override
    public ServerResponse outSubject(Integer id) {
        try{
            subjectMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            return  ServerResponse.createByErrorMessage("不能删除该科目，该科目在其他地方还有引用");
        }
        teacherSubjectMapper.deleteSubjectBySubjectId(id);
        return ServerResponse.createBySuccessMessage("删除成功");
    }

    @Override
    public ServerResponse reviseSubject(Subject subject) {
        Date update_time=new Date();
        subject.setUpdateTime(update_time);
        Subject subject1=subjectMapper.getOneSuject(subject.getName());
        if(subject1!=null){
            return  ServerResponse.createByErrorMessage("科目名称已存在");
        }
        if(subjectMapper.updateByPrimaryKeySelective(subject)==1){
            return ServerResponse.createBySuccessMessage("修改成功");
        }
        return ServerResponse.createByErrorMessage("修改失败");
    }

    @Override
    public ServerResponse getSubjectQuestion(Integer subjectId) {
        return ServerResponse.createBySuccess(questionMapper.selectQuestionsByCondition(subjectId,null,null,null));
    }


}
