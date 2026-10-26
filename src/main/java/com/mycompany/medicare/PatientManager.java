/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.medicare;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author User
 */

public class PatientManager {
    private final ArrayList<Patient> patients = new ArrayList<>();

    /** Registers a new patient. Throws if the Patient ID already exists. */
    public void registerPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        if (searchById(patient.getPatientId()) != null) {
            throw new IllegalArgumentException("A patient with ID " + patient.getPatientId() + " already exists.");
        }
        patients.add(patient);
    }

    /** Returns the patient with the given ID, or null if not found. */
    public Patient searchById(String patientId) {
        if (patientId == null) {
            return null;
        }
        String wanted = patientId.trim();
        for (Patient p : patients) {
            if (p.getPatientId().equalsIgnoreCase(wanted)) {
                return p;
            }
        }
        return null;
    }

    /** Updates the details of an existing patient. Throws if the patient does not exist. */
    public void updatePatient(String patientId, String firstName, String lastName,
                              int age, String gender, String medicalCondition) {
        Patient p = searchById(patientId);
        if (p == null) {
            throw new IllegalArgumentException("No patient found with ID " + patientId + ".");
        }
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setAge(age);
        p.setGender(gender);
        p.setMedicalCondition(medicalCondition);
    }

    /** Deletes a patient. Returns true if a patient was removed. */
    public boolean deletePatient(String patientId) {
        Patient p = searchById(patientId);
        return p != null && patients.remove(p);
    }

    /** Returns a copy of the patient list. */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    public int getTotalPatients() {
        return patients.size();
    }

    /** Returns a new list sorted by Patient ID. */
    public List<Patient> getSortedById() {
        Patient[] array = patients.toArray(new Patient[0]);
        sortArray(array, false);
        return toList(array);
    }

    /** Returns a new list sorted by surname (then first name). */
    public List<Patient> getSortedBySurname() {
        Patient[] array = patients.toArray(new Patient[0]);
        sortArray(array, true);
        return toList(array);
    }

    /** Bubble sort on an array passed to the method; uses the array's length field. */
    public static void sortArray(Patient[] array, boolean bySurname) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (compare(array[j], array[j + 1], bySurname) > 0) {
                    Patient temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    private static int compare(Patient a, Patient b, boolean bySurname) {
        if (bySurname) {
            int result = a.getLastName().compareToIgnoreCase(b.getLastName());
            return result != 0 ? result : a.getFirstName().compareToIgnoreCase(b.getFirstName());
        }
        return a.getPatientId().compareToIgnoreCase(b.getPatientId());
    }

    private static List<Patient> toList(Patient[] array) {
        List<Patient> list = new ArrayList<>();
        for (Patient p : array) {
            list.add(p);
        }
        return list;
    }
}
