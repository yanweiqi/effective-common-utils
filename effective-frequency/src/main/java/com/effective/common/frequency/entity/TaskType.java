package com.effective.common.frequency.entity;

public enum TaskType {

    AB(1, "ab任务"),

    RACING(2, "赛马任务"),

    BLANK(3, "空白"),

    ALL(4, "全量");

    private Integer k;
    private String v;

    TaskType(Integer k, String v) {
        this.k = k;
        this.v = v;
    }

    public Integer getK() {
        return k;
    }

    public String getV() {
        return v;
    }
}
