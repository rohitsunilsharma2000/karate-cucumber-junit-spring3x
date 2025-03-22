package com.example.karate_cucumber_junit_spring3x.opd;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
@ToString

public class PatientManagement {
    private final Map<Long, Patient> patientsDatabase = new HashMap<>();

    public void addPatient(Patient patient) {
        patientsDatabase.put(patient.getPatientId(), patient);
        System.out.println("Patient added: " + patient);
    }

    public void updatePatientHistory(Long patientId, String allergies, String diseases, String pastTreatments) {
        Patient patient = patientsDatabase.get(patientId);
        if (patient != null) {
            patient.updateMedicalHistory(allergies, diseases, pastTreatments);
            System.out.println("Updated medical history for patient ID " + patientId);
        } else {
            System.out.println("Patient ID " + patientId + " not found.");
        }
    }

    public void recordPatientVisit(Long patientId, Appointment appointment) {
        Patient patient = patientsDatabase.get(patientId);
        if (patient != null) {
            patient.addVisit(appointment);
            System.out.println("Visit recorded for patient ID " + patientId);
        } else {
            System.out.println("Patient ID " + patientId + " not found.");
        }
    }

    public void displayPatientDetails(Long patientId) {
        Patient patient = patientsDatabase.get(patientId);
        if (patient != null) {
            System.out.println(patient);
        } else {
            System.out.println("Patient ID " + patientId + " not found.");
        }
    }



}