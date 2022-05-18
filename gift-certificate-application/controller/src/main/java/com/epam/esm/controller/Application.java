package com.epam.esm.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
@EntityScan("com.epam.esm")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
