package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.StudentMapper;
import com.explore.dao.TeacherMapper;
import com.explore.pojo.Student;
import com.explore.pojo.Teacher;
import com.explore.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TeacherServicempl implements ITeacherService {
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    StudentMapper studentMapper;

    @Override
    public ServerResponse<Teacher> login(String username, String password) {
        Teacher teacher = teacherMapper.login(username, password);
        if (teacher == null) {
            return ServerResponse.createByErrorMessage("登陆失败");
        }
        return ServerResponse.createBySuccessMessage("登陆成功",teacher);
    }

    @Override
    public ServerResponse revise(String username, String oldPassword, String newPassword) {
        Teacher teacher = teacherMapper.login(username, oldPassword);
        if (teacher == null) {
            return ServerResponse.createByErrorMessage("用户名密码错误");
        }
        teacher.setPassword(newPassword);
        Date update_time = new Date();
        teacher.setUpdateTime(update_time);
        int count = teacherMapper.updateByPrimaryKey(teacher);
        if (count == 1) {
            return ServerResponse.createBySuccessMessage("修改密码成功");
        } else
            return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse getStudent(String sno) {
        Student student=studentMapper.selectSno(sno);
        if(student!=null){
            return ServerResponse.createBySuccess(student);
        }
        return  ServerResponse.createByErrorMessage("找不到个该学生");
    }

}

