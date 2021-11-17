package com.baomidou.mybatisplus.samples.crud.entity;

import lombok.Data;

@Data
public class Channel extends Operator {

    private Integer id;

    private String name;

    private Integer qps;

    private Integer frequencyId;

    private Integer keywordId;

    private Integer blackListId;

    private Integer whiteListId;

    private Integer yn;
}
