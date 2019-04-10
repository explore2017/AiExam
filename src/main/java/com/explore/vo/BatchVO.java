package com.explore.vo;

import com.explore.pojo.Batch;
import lombok.Data;

/**
 * @author PinTeh
 * @date 2019/4/10
 */
@Data
public class BatchVO extends Batch {
    private Boolean hasSelected;
}
