package com.effective.common.springboot.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class Index {

    @RequestMapping("/getp")
    public String getP(){
        return "cc";
    }
}
