package com.effective.common.frequency.entity;

import lombok.Data;

@Data
public class Effect extends Operator {

    private Integer id;

    private String name;

    private Double clickRate;

    private Double touchRate;


}
