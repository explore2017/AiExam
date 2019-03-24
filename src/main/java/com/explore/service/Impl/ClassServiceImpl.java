package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.*;
import com.explore.pojo.*;
import com.explore.pojo.Class;
import com.explore.service.IClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public ServerResponse<List<Student>> getClassDetail(Integer id) {
        if(classMapper.selectByPrimaryKey(id)==null){
            studentClassMapper.deleteClassByClassId(id);
            return ServerResponse.createByErrorMessage("找不到该班级");
        }
        List<StudentClass> studentClassList=studentClassMapper.selectByClassId(id);
        List<Student> studentList=new ArrayList<>();
        for(StudentClass studentClass:studentClassList){
            Student student=studentMapper.selectByPrimaryKey(studentClass.getStudentId());
            if(student!=null){
                student.setPassword(StringUtils.EMPTY);
                studentList.add(student);
            }
        }
        return ServerResponse.createBySuccess(studentList);
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
        classes.setNumber(0);
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

    @Override
    public ServerResponse deleteStudent(Integer studentId, Integer classId) {
        if(studentMapper.selectByPrimaryKey(studentId)==null){
            return ServerResponse.createByErrorMessage("找不到该学生");
        }
        Class class1=classMapper.selectByPrimaryKey(classId);
        if(class1==null){
            return ServerResponse.createByErrorMessage("找不到该班级");
        }
        class1.setNumber(class1.getNumber()-1);
        if(studentClassMapper.deleteStudent(studentId,classId)==1&&classMapper.updateByPrimaryKeySelective(class1)==1){
            return  ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    @Override
    public ServerResponse addStudent(StudentClass studentClass) {
        if(studentMapper.selectByPrimaryKey(studentClass.getStudentId())==null){
            return ServerResponse.createByErrorMessage("找不到该学生");
        }
        Class class1=classMapper.selectByPrimaryKey(studentClass.getClassId());
        if(class1==null){
            return ServerResponse.createByErrorMessage("找不到该班级");
        }
        if(studentClassMapper.check(studentClass.getStudentId(),studentClass.getClassId())!=null){
            return ServerResponse.createByErrorMessage("该学生已在该班级");
        }
        class1.setNumber(class1.getNumber()+1);
        if(studentClassMapper.insertStudentClass(studentClass)==1&&classMapper.updateByPrimaryKeySelective(class1)==1){
            return ServerResponse.createBySuccessMessage("添加学生成功");
        }
        return ServerResponse.createByErrorMessage("添加学生失败");
    }
}
