package com.baomidou.mybatisplus.samples.crud.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Date;

@Data
public class Operator {

    private String erp;

    //@TableField(fill = FieldFill.INSERT, select = true)
    private Date createTime;

    //@TableField(fill = FieldFill.INSERT_UPDATE, select = true)
    private Date updateTime;
}
