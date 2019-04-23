package com.explore.service.Impl;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.dao.BatchStudentMapper;
import com.explore.pojo.BatchStudent;
import com.explore.service.IBatchStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PinTeh
 * @date 2019/4/23
 */
@Service
public class BatchStudentServiceImpl implements IBatchStudentService {

    @Autowired
    private BatchStudentMapper batchStudentMapper;

    @Override
    public boolean checkIsEnroll(Integer id, Integer batchId) {
        int count = batchStudentMapper.checkHasEnroll(id,batchId);
        return count > 0;
    }

    @Override
    public ServerResponse signIn(Integer id, Integer batchId) {
        BatchStudent batchStudent = batchStudentMapper.selectByStudentIdAndBatchId(id,batchId);
        if (batchStudent==null){
            return ServerResponse.createByErrorMessage("未找到该记录");
        }
        if(batchStudent.getStatus()!= Const.BATCH_STUDENT_STATUS.NOT_SIGN.getStatus()){
            return ServerResponse.createByErrorMessage("您已签到");
        }
        batchStudent.setStatus(Const.BATCH_STUDENT_STATUS.HAD_SIGN.getStatus());
        int count = batchStudentMapper.updateByPrimaryKeySelective(batchStudent);
        if (count==0){
            return ServerResponse.createBySuccessMessage("签到成功");
        }
        return ServerResponse.createByErrorMessage("签到异常");
    }
}
