package com.explore.vo;

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
}
