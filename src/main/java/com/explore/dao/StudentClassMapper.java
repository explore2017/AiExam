package com.explore.dao;
import com.explore.pojo.Class;
import com.explore.pojo.StudentClass;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
@Component
public interface StudentClassMapper {
    @Insert("insert into student_class(student_id,class_id) values(#{studentId},#{classId})")
    int insertStudentClass(@Param("studentId")Integer studentId, @Param("classId")Integer classId);

    @Select("select * from student_class where student_id=#{studentId}")
    List<StudentClass> selectByStudentId(Integer studentId);

    @Select("select B.* from student_class as A,class as B where A.student_id=#{studentId} and A.class_id=B.id")
    List<Class> selectClassByStudentId(Integer studentId);

    @Select("select * from student_class where class_id=#{classId}")
    List<StudentClass> selectByClassId(Integer classId);

    @Select("select * from student_class where student_id=#{studentId} and class_id=#{classId}")
    StudentClass check(@Param("studentId")Integer studentId, @Param("classId")Integer classId);

    @Delete("delete from student_class where student_id=#{studentId}")
    int deleteClassByStudentId (Integer studentId);

    @Delete("delete from student_class where class_id=#{classId}")
    int deleteClassByClassId (Integer classId);

    @Delete("delete from student_class where class_id=#{classId} and student_id=#{studentId}")
    int deleteStudent(@Param("studentId")Integer studentId,@Param("classId")Integer classId);

}
