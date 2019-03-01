package com.explore.dao;

import com.explore.pojo.ExamStudent;
import com.explore.pojo.ExamStudentKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ExamStudentMapper {
    int deleteByPrimaryKey(ExamStudentKey key);

    int insert(ExamStudent record);

    int insertSelective(ExamStudent record);

    ExamStudent selectByPrimaryKey(ExamStudentKey key);

    int updateByPrimaryKeySelective(ExamStudent record);

    int updateByPrimaryKey(ExamStudent record);

    int checkHasEnroll(@Param("exam_id") Integer exam_id,@Param("student_id") Integer student_id);

    int updateScore(@Param("key") ExamStudentKey key,@Param("score") Double score);

    List<ExamStudent> selectByStudentId(Integer studentId);

}