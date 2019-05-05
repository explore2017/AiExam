package com.explore.dao;

import com.explore.pojo.Class;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface ClassMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Class record);

    int insertSelective(Class record);

    Class selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Class record);

    int updateByPrimaryKey(Class record);

    List<Class> selectAllClass();

    List<Class> getClassesByStudentID(Integer id);

    List<Class> selectTeacherClass(Integer teacherId);

    Class checkHasEnroll(String classNo);
}