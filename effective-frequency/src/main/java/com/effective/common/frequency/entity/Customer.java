package com.effective.common.frequency.entity;

import lombok.Data;

@Data
public class Customer extends Operator {

    private Integer id;

    private String name;

    private Integer frequencyId;

    private Integer yn;

}
