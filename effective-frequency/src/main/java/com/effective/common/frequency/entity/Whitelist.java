package com.effective.common.frequency.entity;

import lombok.Data;

@Data
public class Whitelist extends Operator {

    private Integer id;

    private String name;

    private String crowdId;

    private Integer yn;
}
