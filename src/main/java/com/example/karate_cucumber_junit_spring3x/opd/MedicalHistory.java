package com.example.karate_cucumber_junit_spring3x.opd;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@ToString

public class MedicalHistory {
    private String allergies;
    private String chronicDiseases ;
    private String pastTreatments;


    public void updateMedicalHistory(String allergies, String diseases,String pastTreatments) {
        this.allergies = allergies;
        this.chronicDiseases = diseases;
        this.pastTreatments = pastTreatments;
    }

}
