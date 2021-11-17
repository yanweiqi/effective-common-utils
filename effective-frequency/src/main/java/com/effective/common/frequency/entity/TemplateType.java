package com.effective.common.frequency.entity;

public enum TemplateType {

    NOTIFICATION(1, "通知"),

    CONTENT(2, "内容"),

    MARKETING(3, "营销");

    private String v;
    private Integer k;

    TemplateType(Integer k, String v) {
        this.k = k;
        this.v = v;
    }

    public String getV() {
        return v;
    }

    public Integer getK() {
        return k;
    }
}
