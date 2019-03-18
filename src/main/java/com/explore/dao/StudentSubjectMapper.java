package com.explore.dao;

import com.explore.pojo.StudentSubject;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface  StudentSubjectMapper {
    @Insert("insert into student_subject(student_id,subject_id) values(#{studentId},#{subjectId})")
    int insertStudentSubject(@Param("studentId")Integer studentId, @Param("subjectId")Integer subjectId);

    @Select("select * from student_subject where student_id=#{studentId}")
    StudentSubject selectBystudentId(Integer studentId);

    @Select("select * from student_subject where student_id=#{studentId} and subject_id=#{subjectId}")
    StudentSubject check(@Param("studentId")Integer studentId, @Param("subjectId")Integer subjectId);

    @Delete("delete from student_subject where student_id=#{studentId}")
    int deleteSubject (Integer studentId);
}
