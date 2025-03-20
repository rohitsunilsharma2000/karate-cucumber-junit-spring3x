package com.example.karate_cucumber_junit_spring3x.opd;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Doctor {

    private long doctorId;
    private String name;
    private String specialization;
    private boolean available   ;


    // Method to update doctor's profile
    public void updateProfile(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
        System.out.println("Doctor ID " + doctorId + " profile updated.");
    }

    // Method to deactivate doctor (mark unavailable)
    public void deactivateDoctor() {
        this.available = false;
        System.out.println("Doctor ID " + doctorId + " is now inactive.");
    }

    // Method to activate doctor (mark available)
    public void activateDoctor() {
        this.available = true;
        System.out.println("Doctor ID " + doctorId + " is now active.");
    }


//    @NoArgsConstructor
//    public Doctor () {
//    }

//    @AllArgsConstructor
//    public Doctor ( long doctorId , String name , String specialization , boolean available ) {
//        this.doctorId = doctorId;
//        this.name = name;
//        this.specialization = specialization;
//        this.available = available;
//    }
}
