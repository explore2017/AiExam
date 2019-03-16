package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.SubjectMapper;
import com.explore.pojo.Subject;
import com.explore.service.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubjectServicelmpl implements ISubjectService {

    @Autowired
    SubjectMapper subjectMapper;

    @Override
    public ServerResponse<List<Subject>> Subject() {
        return ServerResponse.createBySuccess(subjectMapper.getAllSubject());
    }

    @Override
    public ServerResponse addSubject(Subject subject) {
        Date creat_time = new Date();
        subject.setCreateTime(creat_time);
        if(subject==null){
            return ServerResponse.createByErrorMessage("不能为空");
        }
        Subject suject1=subjectMapper.getOneSuject(subject.getSubjectNo());
        if(suject1!=null){
            return  ServerResponse.createByErrorMessage("课程号相同");
        }
        int count =subjectMapper.insert(subject);
        if(count==1) {
            return ServerResponse.createBySuccessMessage("增加成功");
        }
        return ServerResponse.createByErrorMessage("增加失败");
    }

    @Override
    public ServerResponse outSubject(Subject subject) {
        int count=subjectMapper.deleteByPrimaryKey(subject.getId());
        if(count==1){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    @Override
    public ServerResponse reviseSuject(Subject subject) {
        Date update_time=new Date();
        subject.setUpdateTime(update_time);
        int count=subjectMapper.updateByPrimaryKeySelective(subject);
        if(count==1){
            return ServerResponse.createBySuccessMessage("修改成功");
        }
        return ServerResponse.createByErrorMessage("修改失败");
    }


}
