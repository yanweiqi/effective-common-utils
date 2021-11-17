package com.baomidou.mybatisplus.samples.crud.entity;

import lombok.Data;

@Data
public class Cost extends Operator {

    private Integer id;

    private String name;

    private Integer quality;

    private Float price;

    private Integer yn;
}
