package com.example.karate_cucumber_junit_spring3x.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
    private String to;

    private String subject;

    private String body;
}
