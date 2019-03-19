package com.explore.dao;

import com.explore.pojo.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface StudentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);

    Student login(@Param("sno") String sno,@Param("password") String password);

    Student selectSno(String sno);

    int deleteStudent(String sno);

    List<Student> getAllStudent();
}