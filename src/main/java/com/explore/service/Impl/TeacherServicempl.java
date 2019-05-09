package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.StudentMapper;
import com.explore.dao.TeacherMapper;
import com.explore.pojo.Student;
import com.explore.pojo.Teacher;
import com.explore.service.ITeacherService;
import com.explore.utils.ReadExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Override
    public ServerResponse batchImport(MultipartFile file) {
        List<Map<Integer,String>> allData= ReadExcel.readExcelContentByList(file);
        Student student=new Student();
        int time=0;
        for(Map<Integer,String> data:allData ){
            if(studentMapper.selectSno(data.get(0))!=null){
                continue;
            }
            student.setSno(data.get(0));
            student.setPassword(data.get(1));
            student.setName(data.get(2));
           int count= studentMapper.insert(student);
           if(count>0){
               time++;
           }
        }
        if(time>0){
            return ServerResponse.createBySuccessMessage("批量导入学生成功,成功插入学生"+time+"条");
        }
        return  ServerResponse.createByErrorMessage("批量导入学生失败");
    }



}

