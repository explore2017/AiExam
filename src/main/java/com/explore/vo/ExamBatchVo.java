package com.explore.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ExamBatchVo {
    private Integer id;

    //批次ID
    private Integer batchId;

    private String name;

    //批次名称
    private String batchName;

    //批次描述
    private String batchDescribe;

    //批次考试开始时间
    private Date batchStartTime;

    //批次考试结束时间
    private Date batchEndTime;

    //批次状态
    private Integer batchStudentStatus;

    private Integer subjectId;

    private Integer examTypeId;

    private String subscribe;

    private Date startTime;

    private Date endTime;

    private Integer creatorId;

    private Date createTime;

    private Date updateTime;

}
