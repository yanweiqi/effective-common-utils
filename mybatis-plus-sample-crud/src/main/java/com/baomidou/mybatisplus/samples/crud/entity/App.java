package com.baomidou.mybatisplus.samples.crud.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class App extends Operator{

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String appName;

    private Integer osSwitch;

    private String osVersion;

    private Integer appSwitch;

    private String appVersion;

    private String pushProtocolVersion;

    private Integer subscribeSwitch;

    private Integer frequencyId;

    private Integer keywordId;

    private Integer blackListId;

    private Integer whiteListId;

    private Integer yn;
}
