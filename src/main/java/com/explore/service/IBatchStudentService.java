package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.BatchStudent;

/**
 * @author PinTeh
 * @date 2019/4/23
 */
public interface IBatchStudentService {

    boolean checkIsEnroll(Integer id, Integer batchId);

    ServerResponse signIn(Integer id, Integer batchId);

    boolean checkCanStart(Integer id, Integer batchId);

    int updateReplyStart(Integer id, Integer batchId);

    BatchStudent getByBatchIdAndStudentId(Integer id, Integer batchId);
}
