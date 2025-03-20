package com.example.karate_cucumber_junit_spring3x.service;

import com.example.karate_cucumber_junit_spring3x.opd.Doctor;

import java.util.HashMap;
import java.util.Map;

public class DoctorManagement {
    private final Map<Long, Doctor> doctorsTable = new HashMap<>();


    // Add a new doctor
    public void addDoctor(Doctor doctor) {
        doctorsTable.put(doctor.getDoctorId(), doctor);
        System.out.println("Doctor added: " + doctor);
    }


    public void updateDoctor(Long doctorId, String newName, String newSpecialization){
        Doctor doctor = doctorsTable.get(doctorId);
        if (doctor != null) {
            doctor.updateProfile(newName, newSpecialization);
        }else{
            System.out.println("Doctor ID " + doctorId + " not found.");
        }
    }

    // Remove doctor (delete from the system)
    public void removeDoctor(Long doctorId) {
        if (doctorsTable.containsKey(doctorId)) {
            doctorsTable.remove(doctorId);
            System.out.println("Doctor ID " + doctorId + " has been removed.");
        } else {
            System.out.println("Doctor ID " + doctorId + " not found.");
        }
    }

    // Display all doctors
    public void displayDoctors() {
        if (doctorsTable.isEmpty()) {
            System.out.println("No doctors in the system.");
        } else {
            doctorsTable.values().forEach(System.out::println);
        }
    }



}
