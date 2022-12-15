package com.effective.common.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.effective.common.cache"})
public class CacheApp {

    public static void main(String[] args) {
        SpringApplication.run(CacheApp.class, args);
    }
}
