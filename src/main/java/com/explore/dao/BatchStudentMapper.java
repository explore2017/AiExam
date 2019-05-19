package com.explore.dao;

import com.explore.pojo.BatchStudent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface BatchStudentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BatchStudent record);

    int insertSelective(BatchStudent record);

    BatchStudent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BatchStudent record);

    int updateByPrimaryKey(BatchStudent record);

    int checkHasSelected(@Param("studentId") Integer studentId,@Param("batchId") Integer batchId);

    int getBatchSelelectedNumberByBatchId(Integer batchId);

    int checkHasEnroll(@Param("studentId") Integer studentId,@Param("batchId") Integer batchId);

    int cancel(@Param("studentId") Integer studentId,@Param("batchId")Integer batchId);

    List<BatchStudent> selectByStudentId(Integer studentId);

    List<BatchStudent> selectByBatchId(Integer batchId);

    int deleteByBatchId(Integer batchId);

    BatchStudent selectByStudentIdAndBatchId(@Param("studentId") Integer studentId,@Param("batchId") Integer batchId);

    int checkCanStart(@Param("studentId") Integer studentId,@Param("batchId") Integer batchId);

    List<BatchStudent> selectAfterFinishedByStudentId(Integer studentId);

    List<BatchStudent> selectExamScore(Integer examId);

    int checkExamEnd(Integer examId);
}