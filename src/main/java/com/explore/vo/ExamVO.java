package com.explore.vo;

import com.explore.pojo.Batch;
import com.explore.pojo.BatchStudent;
import com.explore.pojo.Class;
import com.explore.pojo.Exam;
import lombok.Data;

import java.util.List;

/**
 * @author PinTeh
 * @date 2019/4/10
 */
@Data
public class ExamVO extends Exam {

    private List<BatchVO> batches;

    private Boolean hasEnroll;

    /**
     * 可以删除
     */
    private Batch batch;

    private BatchStudent batchStudent;

    private Class itsClass;
}
