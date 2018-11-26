package com.effective.common.mybatis.domain;

import java.io.Serializable;

/**
 * Created by yanweiqi on 2016/5/25.
 */
public class Account implements Serializable {

    private static final long serialVersionUID = 6185862961624213864L;

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
