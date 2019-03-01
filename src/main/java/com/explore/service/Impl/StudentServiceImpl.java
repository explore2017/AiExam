package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.StudentMapper;
import com.explore.pojo.Student;
import com.explore.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    public ServerResponse<Student> login(String sno, String password) {
        Student student = studentMapper.login(sno, password);
        if (student == null) {
            return ServerResponse.createByErrorMessage("学号或密码错误,请重新尝试");
        }
        student.setPassword("");
        return ServerResponse.createBySuccessMessage("登录成功", student);
    }

    @Override
    public ServerResponse revise(String sno, String oldPassword, String newPassword) {
        Student student = studentMapper.login(sno, oldPassword);
        if (student == null) {
            return ServerResponse.createByErrorMessage("学号或密码错误");
        }
        student.setPassword(newPassword);
        Date update_time = new Date();
        student.setUpdateTime(update_time);
        int count = studentMapper.updateByPrimaryKey(student);
        if (count == 1) {
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("修改失败");
    }

    @Override
    public ServerResponse reviseMessage(int id, String newPhone, String newEmail) {
        Student student = studentMapper.selectByPrimaryKey(id);
        student.setPhone(newPhone);
        student.setEmial(newEmail);
        Date update_time = new Date();
        student.setUpdateTime(update_time);
        int count = studentMapper.updateByPrimaryKeySelective(student);
        if (count == 1)
            return ServerResponse.createBySuccessMessage("修改信息成功");
        return ServerResponse.createByErrorMessage("修改失败");
    }

    @Override
    public ServerResponse resetPassword(String sno,String phone,String password) {
        Student student1=studentMapper.selectSno(sno);
        if(student1==null)
            return ServerResponse.createByErrorMessage("学号错误");
        if(student1.getPhone().equals(phone)){
            student1.setPassword(password);
            int count = studentMapper.updateByPrimaryKeySelective(student1);
            if (count == 1)
                return ServerResponse.createBySuccessMessage("修改信息成功");
        }
        return ServerResponse.createByErrorMessage("手机号输入错误");
    }


}
