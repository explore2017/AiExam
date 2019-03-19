package com.explore.dao;

import com.explore.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface TeacherMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Teacher record);

    int insertSelective(Teacher record);

    Teacher selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Teacher record);

    int updateByPrimaryKey(Teacher record);

    Teacher login(@Param("username") String username, @Param("password") String password);

    Teacher selectUsername(String username);

    int deleteStudent(String username);

    List<Teacher> getAllTeacher();
}