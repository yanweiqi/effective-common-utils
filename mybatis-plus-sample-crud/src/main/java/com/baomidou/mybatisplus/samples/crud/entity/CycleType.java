package com.baomidou.mybatisplus.samples.crud.entity;

public enum CycleType {

    SECOND(1,"秒"),

    MINUTE(2,"分"),

    HOUR(3,"时"),

    DAY(4,"天"),

    WEEK(5,"周"),

    MONTH(6,"月"),

    YEAR(7,"年");

    private Integer k;

    private String v;

    CycleType(Integer k, String v) {
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
