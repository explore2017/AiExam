package com.explore.dao;
import com.explore.pojo.StudentClass;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface StudentClassMapper {
    @Insert("insert into student_class(student_id,class_id,class_name) values(#{studentId},#{classId},#{className})")
    int insertStudentClass(@Param("studentId")Integer studentId, @Param("classId")Integer classId,@Param("className")String className);

    @Select("select * from student_class where student_id=#{studentId}")
    List<StudentClass> selectByStudentId(Integer studentId);

    @Select("select * from student_class where class_id=#{classId}")
    List<StudentClass> selectByClassId(Integer classId);

    @Select("select * from student_class where student_id=#{studentId} and class_id=#{classId}")
    StudentClass check(@Param("studentId")Integer studentId, @Param("classId")Integer classId);

    @Delete("delete from student_class where student_id=#{studentId}")
    int deleteClassByStudentId (Integer studentId);

    @Delete("delete from student_class where class_id=#{classId}")
    int deleteClassByClassId (Integer classId);
}
