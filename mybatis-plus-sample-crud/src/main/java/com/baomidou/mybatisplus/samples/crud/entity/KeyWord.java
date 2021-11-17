package com.baomidou.mybatisplus.samples.crud.entity;

import lombok.Data;

@Data
public class KeyWord extends Operator {

    private Integer id;

    private String title;

    private String content;

    private Integer yn;
}
