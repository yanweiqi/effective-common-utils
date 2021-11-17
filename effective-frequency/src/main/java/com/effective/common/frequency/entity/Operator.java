package com.effective.common.frequency.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Operator {

    private String erp;

    private Timestamp createTime;

    private Timestamp updateTime;
}
