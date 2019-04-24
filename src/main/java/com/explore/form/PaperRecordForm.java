package com.explore.form;

import com.explore.vo.PaperQuestionVo;
import lombok.Data;

import java.util.List;

/**
 * @author PinTeh
 * @date 2019/4/24
 */
@Data
public class PaperRecordForm {
    private List<PaperQuestionVo> records;
}
