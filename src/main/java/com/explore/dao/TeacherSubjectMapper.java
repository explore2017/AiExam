package com.explore.dao;

import com.explore.pojo.TeacherSubject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface  TeacherSubjectMapper {
     @Insert("insert into teacher_subject(teacher_id,subject_id) values(#{teacherId},#{subject_id})")
     int insertTeacherSubject(@Param("teacherId")Integer teacherId, @Param("subjectId")Integer subjcetId);
     @Select("select * from teacher_subject where teacher_id=#{teacherId}")
     TeacherSubject selectByteacherId(Integer teacherId);
     @Select("select * from teacher_subject where teacher_id=#{teacherId} and subject_id=#{subjectId}")
     TeacherSubject check(@Param("teacherId")Integer teacherId, @Param("subjectId")Integer subjectId);
}
