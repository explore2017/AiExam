package com.explore.dao;

import com.explore.pojo.Subject;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface SubjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Subject record);

    int insertSelective(Subject record);

    Subject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Subject record);

    int updateByPrimaryKey(Subject record);

    List<Subject> getAllSubject();

    Subject getOneSuject(String name);

    List<Subject> getTeacherSubject(Integer teacherId);
}