package com.example.karate_cucumber_junit_spring3x.opd;

public class OPDMain {
    public static void main ( String[] args ) {

            // Create an instance of PatientManagement to manage patient records
            PatientManagement patientManagement = new PatientManagement();

            // Create a new patient with ID 201, name "Alice Brown", and email "alice@example.com"
            Patient patient1 = new Patient(201, "Alice Brown", "alice@example.com");

            // Add the patient to the patient management system
            patientManagement.addPatient(patient1);

            // Update the medical history of patient with ID 201
            // Allergies: "Peanuts", Chronic Conditions: "Asthma", Past Surgeries: "Past Surgery"
            patientManagement.updatePatientHistory(201L, "Peanuts", "Asthma", "Past Surgery");

            // Create a new appointment for the patient with Dr. John on "2025-03-20"
            Appointment appt1 = new Appointment(301, "Dr. John", "2025-03-20");

            // Record the patient's visit by associating the appointment with patient ID 201
            patientManagement.recordPatientVisit(201L, appt1);

            // Display all details of the patient with ID 201
//            patientManagement.displayPatientDetails(201L);



       // System.out.println("Printing first log");
    }
}
