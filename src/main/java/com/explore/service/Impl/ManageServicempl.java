package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.*;
import com.explore.pojo.*;
import com.explore.service.IManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Transactional
@Service
public class ManageServicempl implements IManageService {
    private static final String STUDENT_TYPE = "student";
    private static final String TEACHER_TYPE = "teacher";
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
    @Autowired
    StudentSubjectMapper studentSubjectMapper;


    @Override
    public ServerResponse<Manager> login(String username, String password) {
        Manager manager = managerMapper.login(username, password);
        if (manager == null) {
            return ServerResponse.createByErrorMessage("用户名或密码错误,请重新尝试");
        }
        manager.setPassword("");
        return ServerResponse.createBySuccessMessage("登录成功", manager);
    }

    @Override
    public ServerResponse revise(String username, String oldPassword, String newPassword) {
        Manager manager = managerMapper.login(username, oldPassword);
        if (manager == null) {
            return ServerResponse.createByErrorMessage("用户名或密码错误");
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
        String[] subjectId=student.getSubjectId().split(",");
        Date creat_time = new Date();
        student.setCreateTime(creat_time);
        Student student1 = studentMapper.selectSno(student.getSno());
        if (student1 != null) {
            return ServerResponse.createByErrorMessage("已有该学号");
        }
        int count = studentMapper.insert(student);
        if (count == 1){
            student1=studentMapper.selectSno(student.getSno());
            ServerResponse serverResponse= add_all_subject(student1.getId(),subjectId,STUDENT_TYPE);
            if(serverResponse.isSuccess()){
                return ServerResponse.createBySuccessMessage("学生增加成功");
            }
        }

        return ServerResponse.createByErrorMessage("学生增加失败");
    }

    @Override
    public ServerResponse outStudent(int id) {
        int count = studentMapper.deleteByPrimaryKey(id);
        int count1=studentSubjectMapper.deleteSubject(id);
        if (count == 1&&count1==1){
            return ServerResponse.createBySuccessMessage("学生删除成功");
        }
        return ServerResponse.createByErrorMessage("学生删除失败");
    }

    @Override
    public ServerResponse reviseStudent(Student student) {
        String[] subjectId=student.getSubjectId().split(",");
        Date update_time = new Date();
        student.setUpdateTime(update_time);
        studentMapper.updateByPrimaryKeySelective(student);
        ServerResponse serverResponse= add_all_subject(student.getId(),subjectId,STUDENT_TYPE);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccessMessage("学生信息修改成功");
        }
        return ServerResponse.createByErrorMessage("学生信息修改失败");
    }

    @Override
    public ServerResponse<List<Teacher>> Teachers() {
        return ServerResponse.createBySuccess(teacherMapper.getAllTeacher());
    }

    @Override
    public ServerResponse addTeacher(Teacher teacher, String[] subject) {
        teacher.setPassword(teacher.getPassword());
        Date creat_time = new Date();
        teacher.setCreateTime(creat_time);
        Teacher teacher1 = teacherMapper.selectUsername(teacher.getUsername());
        if (teacher1 != null) {
            return ServerResponse.createByErrorMessage("已有用户名");
        }
        int count = teacherMapper.insert(teacher);
        teacher1=teacherMapper.selectUsername(teacher.getUsername());
        if (count == 1) {
            ServerResponse serverResponse= add_all_subject(teacher1.getId(),subject,TEACHER_TYPE);
            if(serverResponse.isSuccess()){
                return ServerResponse.createBySuccessMessage("老师增加成功");
            }
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
    public ServerResponse reviseTeacher(Teacher teacher, String[] subject) {
        Date update_time = new Date();
        teacher.setUpdateTime(update_time);
        int count = teacherMapper.updateByPrimaryKeySelective(teacher);
        if (count == 1) {
            int judge = teacherSubjectMapper.deleteSubject(teacher.getId());
            ServerResponse serverResponse=  add_all_subject(teacher.getId(),subject,TEACHER_TYPE);
            if(serverResponse.isSuccess()){
                return ServerResponse.createBySuccessMessage("老师信息修改成功");
            }

        }
        return ServerResponse.createByErrorMessage("老师信息修改失败");
    }

    public ServerResponse add_all_subject(Integer id, String[] subjectId,String type){
        String allSubject = "";
        if (subjectId!=null&&!subjectId[0].equals("")){
            for (int i = 0; i < subjectId.length; i++) {
                Subject subject= subjectMapper.selectByPrimaryKey(Integer.parseInt(subjectId[i]));
                if(subject==null) {return ServerResponse.createByErrorMessage("没有此科目");}
                if(i==0){
                    allSubject+=subject.getName();
                }else{
                    allSubject+=","+subject.getName();
                }
                if(type.equals(TEACHER_TYPE)){
                  teacherSubjectMapper.insertTeacherSubject(id, Integer.parseInt(subjectId[i]));
                }else if(type.equals(STUDENT_TYPE)){
                  studentSubjectMapper.insertStudentSubject(id, Integer.parseInt(subjectId[i]));
                }
            }
            if(type.equals(TEACHER_TYPE)){
              Teacher teacher =new Teacher();
              teacher.setSubjectId(allSubject);
              teacher.setId(id);
             int count=teacherMapper.updateByPrimaryKeySelective(teacher);
                if(count==1){
                    return ServerResponse.createBySuccess();
                }
            }else if(type.equals(STUDENT_TYPE)){
                Student student =new Student();
                student.setSubjectId(allSubject);
                student.setId(id);
               int count=studentMapper.updateByPrimaryKeySelective(student);
                if(count==1){
                    return ServerResponse.createBySuccess();
                }
            }
        }
        return ServerResponse.createByErrorMessage("操作失败");
    }

}

