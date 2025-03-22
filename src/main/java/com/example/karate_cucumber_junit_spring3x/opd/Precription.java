package com.example.karate_cucumber_junit_spring3x.opd;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter


public class Precription {
    private List<String> medications = new ArrayList<>();

    public void addMedication(String medication) {
        medications.add(medication);
    }
}
