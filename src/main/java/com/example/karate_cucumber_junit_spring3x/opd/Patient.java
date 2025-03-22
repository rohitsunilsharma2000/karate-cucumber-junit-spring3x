package com.example.karate_cucumber_junit_spring3x.opd;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Patient {

    private long patientId;
    private String name;
    private String contactInfo;
    private MedicalHistory medicalHistory;
    private List<Appointment> visitHistory;

    public Patient(long patientId, String name, String contactInfo) {

        this.patientId = patientId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.medicalHistory = new MedicalHistory("", "", "");
        this.visitHistory = new ArrayList<>();//initialized
    }

    public void updateMedicalHistory(String allergies, String diseases,String pastTreatments) {
        this.medicalHistory.updateMedicalHistory(allergies,diseases,pastTreatments);
    }
    public void addVisit(Appointment appointment) {
        visitHistory.add(appointment);
    }

}
