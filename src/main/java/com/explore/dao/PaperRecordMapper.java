package com.explore.dao;

import com.explore.pojo.PaperRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaperRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PaperRecord record);

    int insertSelective(PaperRecord record);

    PaperRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaperRecord record);

    int updateByPrimaryKey(PaperRecord record);

    void insertRecords(List<PaperRecord> paperRecords);

    List<PaperRecord> selectByStudentIdAndBatchId(@Param("studentId") Integer studentId, @Param("batchId") Integer batchId);
}