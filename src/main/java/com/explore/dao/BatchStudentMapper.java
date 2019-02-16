package com.explore.dao;

import com.explore.pojo.BatchStudent;
import com.explore.pojo.BatchStudentKey;

public interface BatchStudentMapper {
    int deleteByPrimaryKey(BatchStudentKey key);

    int insert(BatchStudent record);

    int insertSelective(BatchStudent record);

    BatchStudent selectByPrimaryKey(BatchStudentKey key);

    int updateByPrimaryKeySelective(BatchStudent record);

    int updateByPrimaryKey(BatchStudent record);
}