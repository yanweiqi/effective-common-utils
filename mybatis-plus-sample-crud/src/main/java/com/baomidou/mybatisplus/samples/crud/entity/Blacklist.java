package com.baomidou.mybatisplus.samples.crud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blacklist extends Operator {

    private Integer id;

    private String name;

    private String crowdId;

    private Integer yn;
}
