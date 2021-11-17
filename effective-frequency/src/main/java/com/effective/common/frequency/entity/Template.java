package com.effective.common.frequency.entity;

import lombok.Data;

@Data
public class Template {

    private Integer id;

    private String name;

    private String templateId;

    private TemplateType templateType;

    private Integer qps;

    private Integer frequencyId;

    private Integer keywordId;

    private Integer blackListId;

    private Integer whiteListId;

    private Integer yn;

}
