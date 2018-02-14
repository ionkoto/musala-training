package com.musala.simple.students.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.musala.simple.students.spring.web.DbService;

@SpringBootApplication
public class Main {
    private static final String DB_CONNECTION_SUCCESS = "Successfully established connection with both MongoDb and MySQL Databases!";
    private static final String SPRING_BOOT_START_SUCCESS = "Spring boot started successfully!";
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DbService.establishDbConnection();
        logger.info(DB_CONNECTION_SUCCESS);
        SpringApplication.run(Main.class, args);
        logger.info(SPRING_BOOT_START_SUCCESS);
    }
}
