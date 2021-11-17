package com.effective.common.frequency.entity;

import lombok.Data;

@Data
public class Task extends Operator {

    private Integer id;

    private String name;

    private TaskType type;

    private Integer qps;

    private Integer frequencyId;

    private Integer blackListId;

    private Integer whiteListId;

    private Integer yn;

}
