package com.explore.dao;

import com.explore.pojo.Batch;

import java.util.List;

public interface BatchMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Batch record);

    int insertSelective(Batch record);

    Batch selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Batch record);

    int updateByPrimaryKey(Batch record);

    List<Batch> selectBatchesByExamId(Integer exam_id);
}