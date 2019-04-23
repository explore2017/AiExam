package com.explore.dao;

import com.explore.pojo.Batch;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface BatchMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Batch record);

    int insertSelective(Batch record);

    Batch selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Batch record);

    int updateByPrimaryKey(Batch record);

    List<Batch> selectBatchesByExamId(Integer exam_id);

    int selectExamIdByBatchId(Integer batch_id);

    Integer selectPaperIdByBatchId(Integer batchId);
}