package com.effective.common.frequency.entity;

import lombok.Data;

@Data
public class KeyWord extends Operator {

    private Integer id;

    private String title;

    private String content;

    private Integer yn;
}
