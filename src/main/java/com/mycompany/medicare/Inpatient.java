/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.medicare;

/**
 *
 * @author User
 */

public class Inpatient extends Patient {
    private int wardNumber;      // 0 = not assigned
    private String bedNumber;    // e.g. "B07", null = not assigned

    public Inpatient(String patientId, String firstName, String lastName, int age,
                     String gender, String medicalCondition) {
        super(patientId, firstName, lastName, age, gender, medicalCondition, PatientCategory.INPATIENT);
        this.wardNumber = 0;
        this.bedNumber = null;
    }

    public int getWardNumber() { return wardNumber; }
    public String getBedNumber() { return bedNumber; }

    public void setWardNumber(int wardNumber) {
        this.wardNumber = wardNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public boolean hasBed() {
        return bedNumber != null;
    }

    public void clearBed() {
        this.wardNumber = 0;
        this.bedNumber = null;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Ward Number       : " + (hasBed() ? String.valueOf(wardNumber) : "Not allocated"));
        System.out.println("Bed Number        : " + (hasBed() ? bedNumber : "Not allocated"));
    }

    @Override
    public String toSummaryLine() {
        return super.toSummaryLine() + String.format(" Ward %s, Bed %s",
                hasBed() ? String.valueOf(wardNumber) : "-", hasBed() ? bedNumber : "-");
    }
}
