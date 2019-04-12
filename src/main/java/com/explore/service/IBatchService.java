package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Batch;

import java.util.List;

public interface IBatchService {
    ServerResponse getBatchesByExamId(Integer exam_id);

    ServerResponse enroll(Integer batch_id,Integer student_id);

    ServerResponse save(Batch batch);

    ServerResponse delBacth(Integer batchId);

    ServerResponse getBatchDetails(Integer batchId);

    ServerResponse deleteBatchStudent(Integer studentId,Integer batchId);
}
