package com.musala.simple.students.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        StudentService.establishDbConnection();
        SpringApplication.run(Main.class, args);
    }
}
