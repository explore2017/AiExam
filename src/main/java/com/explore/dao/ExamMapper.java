package com.explore.dao;

import com.explore.pojo.BatchStudent;
import com.explore.pojo.Exam;
import com.explore.vo.ExamBatchVo;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface ExamMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Exam record);

    int insertSelective(Exam record);

    Exam selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Exam record);

    int updateByPrimaryKey(Exam record);

    List<Exam> selectExams();

    List<ExamBatchVo> selectExamBatchVoByStudentId(Integer student_id);

    List<Exam> selectExamsByStudentId(Integer id);

    List<Exam> selectTeacherExams(Integer teacherId);


}