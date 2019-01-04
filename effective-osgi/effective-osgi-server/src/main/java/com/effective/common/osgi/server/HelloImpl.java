package com.effective.common.osgi.server;

public class HelloImpl implements Hello {

    private String s;

    public HelloImpl(String s) {
        this.s = s;
    }

    public String sayHello() {
        return s;
    }
}
