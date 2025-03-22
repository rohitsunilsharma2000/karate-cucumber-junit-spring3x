package com.example.karate_cucumber_junit_spring3x.opd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString

public class Diagnosis {
    private String symptoms;
    private String diagnosis;
}
