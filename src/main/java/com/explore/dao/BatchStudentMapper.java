package com.explore.dao;

import com.explore.pojo.BatchStudent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    int cancel(@Param("batchId")Integer batchId,@Param("studentId") Integer studentId);

    List<BatchStudent> selectByStudentId(Integer studentId);
}