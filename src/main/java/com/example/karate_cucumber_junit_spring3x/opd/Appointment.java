package com.example.karate_cucumber_junit_spring3x.opd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.tools.Diagnostic;

@Getter
@AllArgsConstructor
public class Appointment {


    // attributes or member variables
    private  long appointmentId;
    private  String doctorName ;
    private String date;
    private Diagnosis diagnosis;
    private Precription precription;


    public Appointment (long appointmentId, String doctorName, String date) {
        this.appointmentId = appointmentId;
        this.doctorName = doctorName;
        this.date = date;
    }
    public  void addDiagnosis (Diagnosis diagnosis){
        this.diagnosis = diagnosis;
    }

    public void addPrecription (Precription precription){
        this.precription = precription;
    }

}
