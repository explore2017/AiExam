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
            List<StudentClass> studentClasses=studentClassMapper.selectByStudentId(student.getId());
            if(studentClasses!=null){
                for(int i=0;i<studentClasses.size();i++){
                    if(studentClasses.get(i).getClassName()!=null){
                        if(str.equals("")){
                            str=studentClasses.get(i).getClassName();
                        }else{
                            str=str+","+studentClasses.get(i).getClassName();
                        }
                    }
                }
            }
            student.setClasses(str);
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
        int count = studentMapper.deleteByPrimaryKey(id);
        int count1= studentClassMapper.deleteClassByStudentId(id);
        if (count == 1&&count1==1){
            return ServerResponse.createBySuccessMessage("学生删除成功");
        }
        return ServerResponse.createByErrorMessage("学生删除失败");
    }

    @Override
    public ServerResponse reviseStudent(Student student) {
        String[] ClassId=student.getClasses().split(",");
        student.setUpdateTime(new Date());
        if(studentClassMapper.deleteClassByStudentId(student.getId())==1){
            addRelation(student.getId(),ClassId,STUDENT_TYPE);
            if(studentMapper.updateByPrimaryKeySelective(student)==1){
                return ServerResponse.createBySuccessMessage("学生信息修改成功");
            }
        }
        return ServerResponse.createByErrorMessage("学生信息修改失败");
    }

    @Override
    public ServerResponse<List<Teacher>> Teachers() {
        List<Teacher> teachers=teacherMapper.getAllTeacher();
        for(Teacher teacher:teachers){                         //给科目赋值
            String str="";
            List<TeacherSubject> teacherSubjects=teacherSubjectMapper.selectByTeacherId(teacher.getId());
            if(teacherSubjects!=null){
                for(int i=0;i<teacherSubjects.size();i++){
                    if(teacherSubjects.get(i).getSubjectName()!=null)
                    if(str.equals("")){
                        str=teacherSubjects.get(i).getSubjectName();
                    }else{
                        str=str+","+teacherSubjects.get(i).getSubjectName();
                    }
                }
            }
            teacher.setSubjectId(str);
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
        int count = teacherMapper.deleteByPrimaryKey(id);
        int count1=teacherSubjectMapper.deleteSubjectByTeacherId(id);
        if (count == 1&&count1==1) {
            return ServerResponse.createBySuccessMessage("该老师删除成功");
        }
        return ServerResponse.createByErrorMessage("该老师删除失败");
    }

    @Override
    public ServerResponse reviseTeacher(Teacher teacher, String[] subjectId) {
        teacher.setUpdateTime(new Date());
        if ( teacherSubjectMapper.deleteSubjectByTeacherId(teacher.getId())== 1) {
            addRelation(teacher.getId(),subjectId,TEACHER_TYPE);
            if(teacherMapper.updateByPrimaryKeySelective(teacher)==1)
            {
                return ServerResponse.createBySuccessMessage("老师信息修改成功");
            }
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
                  teacherSubjectMapper.insertTeacherSubject(id, Integer.parseInt(relationId[i]),subject.getName());
                }else if(type.equals(STUDENT_TYPE)){
                    Class classes=classMapper.selectByPrimaryKey(Integer.parseInt(relationId[i]));
                    if(classes==null) {continue;}
                  studentClassMapper.insertStudentClass(new StudentClass(id, Integer.parseInt(relationId[i]),classes.getName()));
                }
            }
        }
    }

}

