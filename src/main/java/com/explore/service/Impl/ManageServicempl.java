package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.*;
import com.explore.pojo.*;
import com.explore.pojo.Class;
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
    StudentClassMapper studentClassMapper;
    @Autowired
    ClassMapper classMapper;


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
        List<Student> students=studentMapper.getAllStudent();
        for(Student student:students){                         //给科目赋值
            String str="";
            List<Class> Classes=studentClassMapper.selectClassByStudentId(student.getId());
            if(Classes!=null){
                for(int i=0;i<Classes.size();i++){
                    if(Classes.get(i).getName()!=null){
                        if(str.equals("")){
                            str=Classes.get(i).getName();
                        }else {
                            str+=","+Classes.get(i).getName();
                        }
                    }
                }
            }
            student.setClasses(str);
            student.setPassword("");
        }
        return ServerResponse.createBySuccess(students);
    }

    @Override
    public ServerResponse addStudent(Student student) {
        String[] ClassId=student.getClasses().split(",");
        Date creat_time = new Date();
        student.setCreateTime(creat_time);
        Student student1 = studentMapper.selectSno(student.getSno());
        if (student1 != null) {
            return ServerResponse.createByErrorMessage("已有该学号");
        }
        if (studentMapper.insert(student) == 1){
            student=studentMapper.selectSno(student.getSno());
            addRelation(student.getId(),ClassId,STUDENT_TYPE);    //添加班级
        }

        return ServerResponse.createByErrorMessage("学生增加失败");
    }

    @Override
    public ServerResponse outStudent(int id) {
        studentClassMapper.deleteClassByStudentId(id);
        int count = studentMapper.deleteByPrimaryKey(id);
        if (count == 1){
            return ServerResponse.createBySuccessMessage("学生删除成功");
        }
        return ServerResponse.createByErrorMessage("学生删除失败");
    }

    @Override
    public ServerResponse reviseStudent(Student student) {
        String[] ClassId=student.getClasses().split(",");
        student.setUpdateTime(new Date());
        studentClassMapper.deleteClassByStudentId(student.getId());
        addRelation(student.getId(),ClassId,STUDENT_TYPE);
            if(studentMapper.updateByPrimaryKeySelective(student)==1){
                return ServerResponse.createBySuccessMessage("学生信息修改成功");
            }
        return ServerResponse.createByErrorMessage("学生信息修改失败");
    }

    @Override
    public ServerResponse<List<Teacher>> Teachers() {
        List<Teacher> teachers=teacherMapper.getAllTeacher();
        for(Teacher teacher:teachers){                         //给科目赋值
            String str="";
            List<Subject> subjects=teacherSubjectMapper.selectSubjectByTeacherId(teacher.getId());
            if(subjects!=null){
                for(int i=0;i<subjects.size();i++){
                   if(subjects.get(i).getName()!=null){
                       if(str.equals("")){
                           str=subjects.get(i).getName();
                       }else {
                           str+=","+subjects.get(i).getName();
                       }
                   }
                }
            }
            teacher.setSubjectId(str);
            teacher.setPassword("");
        }
        return ServerResponse.createBySuccess(teachers);
    }

    @Override
    public ServerResponse addTeacher(Teacher teacher, String[] subjectId) {
        teacher.setPassword(teacher.getPassword());
        Date creat_time = new Date();
        teacher.setCreateTime(creat_time);
        Teacher teacher1 = teacherMapper.selectUsername(teacher.getUsername());
        if (teacher1 != null) {
            return ServerResponse.createByErrorMessage("已有用户名");
        }

        if (teacherMapper.insert(teacher) == 1) {
            teacher1=teacherMapper.selectUsername(teacher.getUsername());
            addRelation(teacher1.getId(),subjectId,TEACHER_TYPE);
                return ServerResponse.createBySuccessMessage("老师增加成功");
        }
        return ServerResponse.createByErrorMessage("老师增加失败");
    }

    @Override
    public ServerResponse outTeacher(int id) {
        teacherSubjectMapper.deleteSubjectByTeacherId(id);
        int count = teacherMapper.deleteByPrimaryKey(id);
        if (count == 1) {
            return ServerResponse.createBySuccessMessage("该老师删除成功");
        }
        return ServerResponse.createByErrorMessage("该老师删除失败");
    }

    @Override
    public ServerResponse reviseTeacher(Teacher teacher, String[] subjectId) {
        teacher.setUpdateTime(new Date());
    teacherSubjectMapper.deleteSubjectByTeacherId(teacher.getId());
            addRelation(teacher.getId(),subjectId,TEACHER_TYPE);
            if(teacherMapper.updateByPrimaryKeySelective(teacher)==1)
            {
                return ServerResponse.createBySuccessMessage("老师信息修改成功");
            }
        return ServerResponse.createByErrorMessage("老师信息修改失败");
    }


    @Transactional
    public void addRelation(Integer id, String[] relationId,String type){

        if (relationId!=null&&!relationId[0].equals("")){
            for (int i = 0; i < relationId.length; i++) {   //根据传入值不同循环对关系表添加数据
                if(type.equals(TEACHER_TYPE)){
                    Subject subject= subjectMapper.selectByPrimaryKey(Integer.parseInt(relationId[i]));
                    if(subject==null) {continue;}
                  teacherSubjectMapper.insertTeacherSubject(id, Integer.parseInt(relationId[i]));
                }else if(type.equals(STUDENT_TYPE)){
                    Class classes=classMapper.selectByPrimaryKey(Integer.parseInt(relationId[i]));
                    if(classes==null) {continue;}
                  studentClassMapper.insertStudentClass(id, Integer.parseInt(relationId[i]));
                }
            }
        }
    }

}

