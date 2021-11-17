package com.baomidou.mybatisplus.samples.crud.entity;

import lombok.Data;

@Data
public class Frequency extends Operator {

    private Integer id;

    private String name;

    private String startTime;

    private String endTime;

    private CycleType cycleType;

    private Integer quality;

    private Integer yn;
}
