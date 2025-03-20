package com.example.karate_cucumber_junit_spring3x;

import com.example.karate_cucumber_junit_spring3x.opd.Doctor;
import com.example.karate_cucumber_junit_spring3x.service.DoctorManagement;

public class OPDDoctorManagement {
    public static void main ( String[] args ) {
        DoctorManagement doctorManagement = new DoctorManagement();


        // Adding Doctors
        Doctor doc1 = new Doctor(101 , "Dr. John Smith" , "Cardiology" , true);
        Doctor doc2 = new Doctor(102 , "Dr. Emily Brown" , "Neurology" , true);

        //Saving doctor
        doctorManagement.addDoctor(doc1);
        doctorManagement.addDoctor(doc2);

        // Displaying all doctors
        System.out.println("\nCurrent Doctors:");
        doctorManagement.displayDoctors();


        // Updating a doctor's profile
        System.out.println("\nUpdating Dr. John Smith's specialization:");
        doctorManagement.updateDoctor(101L , "Dr. John Smith" , "General Medicine");


//        doctorManagement.displayDoctors();


        // Deactivating a doctor
        System.out.println("\nDeactivating Dr. Emily Brown:");
        doc2.deactivateDoctor();

//        doctorManagement.displayDoctors();

        // Removing a doctor
        System.out.println("\nRemoving Dr. John Smith:");
        doctorManagement.removeDoctor(101L);


        doctorManagement.displayDoctors();


    }
}
