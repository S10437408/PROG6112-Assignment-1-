/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.medicare;

/**
 *
 * @author User
 */

/**
 * The three categories of patient treated by MediCare Hospital.
 */
public enum PatientCategory {
    INPATIENT("Inpatient"),
    OUTPATIENT("Outpatient"),
    EMERGENCY("Emergency");

    private final String displayName;

    PatientCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Only inpatients may be allocated a hospital bed. */
    public boolean canBeAllocatedBed() {
        return this == INPATIENT;
    }

    /** Converts a menu choice (1-3) to a category. */
    public static PatientCategory fromChoice(int choice) {
        PatientCategory[] values = values();
        if (choice < 1 || choice > values.length) {
            throw new IllegalArgumentException("Category choice must be between 1 and " + values.length + ".");
        }
        return values[choice - 1];
    }

    @Override
    public String toString() {
        return displayName;
    }
}