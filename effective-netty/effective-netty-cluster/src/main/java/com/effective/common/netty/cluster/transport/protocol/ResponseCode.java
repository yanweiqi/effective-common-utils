package com.effective.common.netty.cluster.transport.protocol;

/**
 * Response Code
 *
 */
public enum ResponseCode {

    /**
     * 请求出错
     */
    ERROR(0, "请求出错"),

    /**
     * 请求成功
     */
    OK(1, "请求成功"),

    /**
     * 请求不支持
     */
    NOT_SUPPORT(2, "请求不支持");

    /**
     * code
     */
    private int code;

    /**
     * name
     */
    private String name;

    ResponseCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
