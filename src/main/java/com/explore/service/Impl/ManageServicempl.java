package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.*;
import com.explore.pojo.Manager;
import com.explore.pojo.Student;
import com.explore.pojo.Teacher;
import com.explore.service.IManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.explore.pojo.TeacherSubject;

import java.util.Date;
import java.util.List;

@Service
public class ManageServicempl implements IManageService {

    @Autowired
    ManagerMapper managerMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    SubjectMapper subjectMapper;
    @Autowired
    TeacherSubjectMapper teacherSubjectMapper;

    @Override
    public ServerResponse<Manager> login(String username, String password) {
        Manager manager = managerMapper.login(username, password);
        if (manager == null) {
            return ServerResponse.createByErrorMessage("学号或密码错误,请重新尝试");
        }
        manager.setPassword("");
        return ServerResponse.createBySuccessMessage("登录成功", manager);
    }

    @Override
    public ServerResponse revise(String username, String oldPassword, String newPassword) {
        Manager manager = managerMapper.login(username, oldPassword);
        if (manager == null) {
            return ServerResponse.createByErrorMessage("学号或密码错误");
        }
        manager.setPassword(newPassword);
        int count = managerMapper.updateByPrimaryKey(manager);
        if (count == 1) {
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("修改失败");
    }

    @Override
    public ServerResponse<List<Student>> Students() {
        return ServerResponse.createBySuccess(studentMapper.getAllStudent());
    }

    @Override
    public ServerResponse addStudent(Student student) {
        student.setPassword("123456");
        Date creat_time = new Date();
        student.setCreateTime(creat_time);
        Student student1 = studentMapper.selectSno(student.getSno());
        if (student1 != null) {
            return ServerResponse.createByErrorMessage("已有该学号");
        }
        int count = studentMapper.insert(student);
        if (count == 1)
            return ServerResponse.createBySuccessMessage("学生增加成功");
        return ServerResponse.createByErrorMessage("学生增加失败");
    }

    @Override
    public ServerResponse outStudent(int id) {
        int count = studentMapper.deleteByPrimaryKey(id);
        if (count == 1)
            return ServerResponse.createBySuccessMessage("学生删除成功");
        return ServerResponse.createByErrorMessage("学生删除失败");
    }

    @Override
    public ServerResponse reviseStudent(Student student) {
        Date update_time = new Date();
        student.setUpdateTime(update_time);
        int count = studentMapper.updateByPrimaryKeySelective(student);
        if (count == 1)
            return ServerResponse.createBySuccessMessage("学生信息修改成功");
        return ServerResponse.createByErrorMessage("学生信息修改失败");
    }

    @Override
    public ServerResponse<List<Teacher>> Teachers() {
        return ServerResponse.createBySuccess(teacherMapper.getAllTeacher());
    }

    @Override
    public ServerResponse addTeacher(Teacher teacher, int[] subject) {
        teacher.setPassword(teacher.getPassword());
        Date creat_time = new Date();
        teacher.setCreateTime(creat_time);
        Teacher teacher1 = teacherMapper.selectUsername(teacher.getUsername());
        if (teacher1 != null)
            return ServerResponse.createByErrorMessage("已有用户名");
        int count = teacherMapper.insert(teacher);
        teacher1=teacherMapper.selectUsername(teacher.getUsername());
        if (count == 1) {
            add_all_subject(teacher1,subject);
            return ServerResponse.createBySuccessMessage("老师增加成功");
        }
        return ServerResponse.createByErrorMessage("老师增加失败");
    }

    @Override
    public ServerResponse outTeacher(int id) {
        int count = teacherMapper.deleteByPrimaryKey(id);
        if (count == 1) {
            return ServerResponse.createBySuccessMessage("该老师删除成功");
        }
        return ServerResponse.createByErrorMessage("该老师删除失败");
    }

    @Override
    public ServerResponse reviseTeacher(Teacher teacher, int[] subject) {
        Date update_time = new Date();
        teacher.setUpdateTime(update_time);
        int count = teacherMapper.updateByPrimaryKeySelective(teacher);
        if (count == 1) {
                int judge = teacherSubjectMapper.deleteSubject(teacher.getId());
            add_all_subject(teacher,subject);
            return ServerResponse.createBySuccessMessage("老师信息修改成功");
        }
        return ServerResponse.createByErrorMessage("老师信息修改失败");
    }

    public void add_all_subject(Teacher teacher, int[] subject){
        String allSubject = "";
        for (int i = 0; i < subject.length; i++) {
            teacherSubjectMapper.insertTeacherSubject(teacher.getId(), subject[i]);
        }
        teacher.setSubjectId(allSubject);
        teacherMapper.updateByPrimaryKeySelective(teacher);
    }

}

