package com.explore.vo;

import com.explore.pojo.Batch;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author PinTeh
 * @date 2019/4/10
 */
@Data
public class BatchVO extends Batch {
    //当前用户是否报名了该批次
    private Boolean hasSelected;

    //当前批次的选择人数
    private Integer selectedNumber;

    private Boolean isFull;
}
