package com.example.graph;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraphIntegrityApp {
    private static final Logger logger = LoggerFactory.getLogger(GraphIntegrityApp.class);

    public static void main(String[] args) {
        logger.info("Starting Graph Application...");
        SpringApplication.run(GraphIntegrityApp.class, args);
        logger.info("Graph Application started successfully.");
    }
}

