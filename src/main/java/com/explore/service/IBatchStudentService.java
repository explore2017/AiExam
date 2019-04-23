package com.explore.service;

import com.explore.common.ServerResponse;

/**
 * @author PinTeh
 * @date 2019/4/23
 */
public interface IBatchStudentService {

    boolean checkIsEnroll(Integer id, Integer batchId);

    ServerResponse signIn(Integer id, Integer batchId);
}
