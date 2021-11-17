package com.baomidou.mybatisplus.samples.crud.entity;

import lombok.Data;

@Data
public class Customer extends Operator {

    private Integer id;

    private String name;

    private Integer frequencyId;

    private Integer yn;

}
