package com.explore.dao;

import com.explore.pojo.Subject;
import com.explore.pojo.TeacherSubject;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface  TeacherSubjectMapper {
     @Insert("insert into teacher_subject(teacher_id,subject_id) values(#{teacherId},#{subjectId})")
     int insertTeacherSubject(@Param("teacherId")Integer teacherId, @Param("subjectId")Integer subjectId);

     @Select("select * from teacher_subject where teacher_id=#{teacherId}")
     List<TeacherSubject> selectByTeacherId(Integer teacherId);

     @Select("select B.* from teacher_subject as A ,`subject` as B  where teacher_id=#{teacherId} and A.subject_id=B.id ")
     List<Subject> selectSubjectByTeacherId(Integer teacherId);

     @Select("select * from teacher_subject where subject_id=#{subjectId}")
     List<TeacherSubject> selectBySubjectId(Integer subjectId);

     @Select("select * from teacher_subject where teacher_id=#{teacherId} and subject_id=#{subjectId}")
     TeacherSubject check(@Param("teacherId")Integer teacherId, @Param("subjectId")Integer subjectId);

     @Delete("delete from teacher_subject where teacher_id=#{teacherId}")
     int deleteSubjectByTeacherId (Integer teacherId);

     @Delete("delete from teacher_subject where subject_id=#{subjectId}")
     int deleteSubjectBySubjectId (Integer subjectId);
}
