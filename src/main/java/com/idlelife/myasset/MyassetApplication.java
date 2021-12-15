package com.idlelife.myasset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MyassetApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyassetApplication.class, args);
    }
}
