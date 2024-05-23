package com.example.taskblock2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.taskblock2.data")
public class TaskBlock2Application {

    public static void main(String[] args) {
        SpringApplication.run(TaskBlock2Application.class, args);
    }

}
