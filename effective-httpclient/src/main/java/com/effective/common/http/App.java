package com.effective.common.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * Created by yanweiqi on 2018/11/30.
 */
@SpringBootApplication
@Component(value="com.effective.common.http.controller")
public class App {

    public static void main(String[] args){
        SpringApplication.run(App.class,args);
    }
}
