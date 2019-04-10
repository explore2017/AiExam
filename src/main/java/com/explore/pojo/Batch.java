package com.explore.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Batch {
    private Integer id;

    private Integer examId;

    private String name;

    private String describe;

    private Date startTime;

    private Date endTime;

    private Integer maxNumber;

    private Integer paperId;

}