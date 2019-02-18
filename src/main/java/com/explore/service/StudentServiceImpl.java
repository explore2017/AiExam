package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.dao.StudentMapper;
import com.explore.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    public ServerResponse<Student> login(String sno, String password) {
        Student student = studentMapper.login(sno,password);
        if (student==null){
            return ServerResponse.createByErrorMessage("学号或密码错误,请重新尝试");
        }
        return ServerResponse.createBySuccessMessage("登录成功",student);
    }
}
