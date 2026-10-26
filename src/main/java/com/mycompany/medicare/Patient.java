/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.medicare;

/**
 *
 * @author User
 */
public class Patient {
    private String patientId;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String medicalCondition;
    private PatientCategory category;

    public Patient(String patientId, String firstName, String lastName, int age,
                   String gender, String medicalCondition, PatientCategory category) {
        setPatientId(patientId);
        setFirstName(firstName);
        setLastName(lastName);
        setAge(age);
        setGender(gender);
        setMedicalCondition(medicalCondition);
        if (category == null) {
            throw new IllegalArgumentException("Patient category is required.");
        }
        this.category = category;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    public String getPatientId() { return patientId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getMedicalCondition() { return medicalCondition; }
    public PatientCategory getCategory() { return category; }

    public void setPatientId(String patientId) {
        this.patientId = requireText(patientId, "Patient ID").toUpperCase();
    }

    public void setFirstName(String firstName) {
        this.firstName = requireText(firstName, "First name");
    }

    public void setLastName(String lastName) {
        this.lastName = requireText(lastName, "Last name");
    }

    public void setAge(int age) {
        if (age < 0 || age > 120) {
            throw new IllegalArgumentException("Age must be between 0 and 120.");
        }
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = requireText(gender, "Gender");
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = requireText(medicalCondition, "Medical condition");
    }

    /** Prints the patient's details to the console. */
    public void displayDetails() {
        System.out.println("Patient ID        : " + patientId);
        System.out.println("Name              : " + firstName + " " + lastName);
        System.out.println("Age               : " + age);
        System.out.println("Gender            : " + gender);
        System.out.println("Medical Condition : " + medicalCondition);
        System.out.println("Category          : " + category.getDisplayName());
    }

    /** One-line summary used in tables and reports. */
    public String toSummaryLine() {
        return String.format("%-8s %-12s %-14s %-4d %-8s %-18s %-10s",
                patientId, firstName, lastName, age, gender, medicalCondition, category.getDisplayName());
    }
}