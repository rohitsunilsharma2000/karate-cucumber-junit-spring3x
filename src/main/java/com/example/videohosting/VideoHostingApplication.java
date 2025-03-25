package com.example.videohosting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VideoHostingApplication {

    public static void main ( String[] args ) {
        SpringApplication.run(VideoHostingApplication.class , args);
    }
}