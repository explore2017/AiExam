package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.*;
import com.explore.pojo.*;
import com.explore.pojo.Class;
import com.explore.service.IClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class ClassServiceImpl implements IClassService {
    @Autowired
    ClassMapper classMapper;
    @Autowired
    StudentClassMapper studentClassMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    SubjectMapper subjectMapper;
    @Autowired
    TeacherMapper teacherMapper;
    @Override
    public ServerResponse<List<Class>> getClasses() {
        return ServerResponse.createBySuccess(classMapper.selectAllClass());
    }

    @Override
    public ServerResponse addClass(Class classes) {
        Subject subject=subjectMapper.selectByPrimaryKey(classes.getSubjectId());
        if(subject==null){return ServerResponse.createByErrorMessage("找不到该科目");}
        Teacher teacher=teacherMapper.selectByPrimaryKey(classes.getTeacherId());
        if(teacher==null){return ServerResponse.createByErrorMessage("找不到该老师");}
        classes.setSubjectName(subject.getName());
        classes.setTeacherName(teacher.getName());
        classes.setCreateTime(new Date());
        if(classMapper.insert(classes)==1){
            return ServerResponse.createBySuccessMessage("创建班级成功");
        }
        return ServerResponse.createByErrorMessage("创建班级失败");
    }

    @Override
    public ServerResponse deleteClass(Integer id) {
           if(classMapper.selectByPrimaryKey(id)==null){   return ServerResponse.createByErrorMessage("找不到此班级");}
            studentClassMapper.deleteClassByClassId(id);
            if(classMapper.deleteByPrimaryKey(id)==1){
                return ServerResponse.createBySuccessMessage("删除成功");
            }

        return ServerResponse.createByErrorMessage("删除失败");
    }

    @Override
    public ServerResponse reviseClass(Class classes) {
        classes.setUpdateTime(new Date());
        if(classMapper.updateByPrimaryKeySelective(classes)==1){
            return ServerResponse.createBySuccessMessage("修改班级成功");
        }
        return ServerResponse.createByErrorMessage("修改班级失败");
    }
}
