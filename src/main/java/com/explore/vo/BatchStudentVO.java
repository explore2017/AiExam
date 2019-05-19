package com.explore.vo;

import com.explore.common.Const;
import com.explore.pojo.Batch;
import com.explore.pojo.BatchStudent;
import com.explore.pojo.Exam;
import lombok.Data;

/**
 * @author PinTeh
 * @date 2019/4/22
 */
@Data
public class BatchStudentVO extends BatchStudent {

    private Batch batch;

    private Exam exam;

    private  Boolean examPaperStatus;
}
