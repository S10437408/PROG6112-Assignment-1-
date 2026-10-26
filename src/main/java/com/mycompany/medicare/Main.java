/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.medicare;

import java.util.List;
import java.util.Scanner;
/**
 *
 * @author User
 */

/**
 * Console, menu-driven entry point for the MediCare Hospital Patient Admission System.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PatientManager patientManager = new PatientManager();
    private static final Ward ward = new Ward();

    public static void main(String[] args) {
        System.out.println("=== MediCare Hospital Patient Admission System ===");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> registerPatient();
                    case 2 -> searchPatient();
                    case 3 -> updatePatient();
                    case 4 -> deletePatient();
                    case 5 -> displayAllPatients();
                    case 6 -> allocateBed();
                    case 7 -> releaseBed();
                    case 8 -> ward.displayLayout();
                    case 9 -> ward.displayAvailableBeds();
                    case 10 -> ward.displayOccupiedBeds();
                    case 11 -> displayReports();
                    case 12 -> displaySorted();
                    case 0 -> {
                        System.out.println("Goodbye.");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please select 0-12.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n---------------- MENU ----------------");
        System.out.println(" 1. Register a new patient");
        System.out.println(" 2. Search for a patient by ID");
        System.out.println(" 3. Update patient details");
        System.out.println(" 4. Delete a patient");
        System.out.println(" 5. Display all registered patients");
        System.out.println(" 6. Allocate a bed to an inpatient");
        System.out.println(" 7. Release a bed (discharge)");
        System.out.println(" 8. Display ward layout");
        System.out.println(" 9. Display available beds");
        System.out.println("10. Display occupied beds");
        System.out.println("11. Reports");
        System.out.println("12. Display patients sorted (by surname or ID)");
        System.out.println(" 0. Exit");
        System.out.println("--------------------------------------");
    }

    // ---------- Patient features ----------

    private static void registerPatient() {
        System.out.println("\n--- Register New Patient ---");
        String id = readLine("Patient ID: ");
        String first = readLine("First name: ");
        String last = readLine("Last name: ");
        int age = readInt("Age: ");
        String gender = readLine("Gender: ");
        String condition = readLine("Medical condition: ");
        System.out.println("Category: 1) Inpatient  2) Outpatient  3) Emergency");
        PatientCategory category = PatientCategory.fromChoice(readInt("Choose category: "));

        Patient patient;
        if (category == PatientCategory.INPATIENT) {
            patient = new Inpatient(id, first, last, age, gender, condition);
        } else {
            patient = new Patient(id, first, last, age, gender, condition, category);
        }
        patientManager.registerPatient(patient);
        System.out.println("Patient registered successfully.");
        if (category.canBeAllocatedBed()) {
            System.out.println("Use option 6 to allocate this inpatient a bed.");
        }
    }

    private static void searchPatient() {
        String id = readLine("Enter Patient ID to search: ");
        Patient p = patientManager.searchById(id);
        if (p == null) {
            System.out.println("No patient found with ID " + id.trim().toUpperCase() + ".");
        } else {
            System.out.println();
            p.displayDetails();
        }
    }

    private static void updatePatient() {
        String id = readLine("Enter Patient ID to update: ");
        Patient existing = patientManager.searchById(id);
        if (existing == null) {
            System.out.println("No patient found with ID " + id.trim().toUpperCase() + ".");
            return;
        }
        System.out.println("Current details:");
        existing.displayDetails();
        System.out.println("\nEnter the new details:");
        String first = readLine("First name: ");
        String last = readLine("Last name: ");
        int age = readInt("Age: ");
        String gender = readLine("Gender: ");
        String condition = readLine("Medical condition: ");
        patientManager.updatePatient(id, first, last, age, gender, condition);
        System.out.println("Patient updated successfully.");
    }

    private static void deletePatient() {
        String id = readLine("Enter Patient ID to delete: ");
        Patient p = patientManager.searchById(id);
        if (p == null) {
            System.out.println("No patient found with ID " + id.trim().toUpperCase() + ".");
            return;
        }
        // Free the bed first if the patient is an inpatient occupying one.
        if (p instanceof Inpatient inpatient && inpatient.hasBed()) {
            String bed = inpatient.getBedNumber();
            ward.releaseBed(bed);
            System.out.println("Bed " + bed + " has been released.");
        }
        patientManager.deletePatient(id);
        System.out.println("Patient deleted successfully.");
    }

    private static void displayAllPatients() {
        List<Patient> all = patientManager.getAllPatients();
        System.out.println("\n--- All Registered Patients (" + all.size() + ") ---");
        if (all.isEmpty()) {
            System.out.println("No patients registered.");
            return;
        }
        for (Patient p : all) {
            System.out.println(p.toSummaryLine());
        }
    }

    // ---------- Bed features ----------

    private static void allocateBed() {
        if (ward.isFull()) {
            System.out.println("No beds are available. The ward is full.");
            return;
        }
        String id = readLine("Enter Patient ID: ");
        Patient p = patientManager.searchById(id);
        if (p == null) {
            System.out.println("No patient found with ID " + id.trim().toUpperCase() + ".");
            return;
        }
        if (!p.getCategory().canBeAllocatedBed() || !(p instanceof Inpatient)) {
            System.out.println("Only inpatients may be allocated a hospital bed.");
            return;
        }
        ward.displayAvailableBeds();
        String bed = readLine("Enter bed number (e.g. B05): ");
        ward.allocateBed((Inpatient) p, bed);
        System.out.println("Bed " + ((Inpatient) p).getBedNumber() + " allocated to "
                + p.getFirstName() + " " + p.getLastName() + ".");
    }

    private static void releaseBed() {
        ward.displayOccupiedBeds();
        String bed = readLine("Enter bed number to release: ");
        Inpatient released = ward.releaseBed(bed);
        System.out.println("Bed " + bed.trim().toUpperCase() + " released. "
                + released.getFirstName() + " " + released.getLastName()
                + " has been discharged from the bed.");
    }

    // ---------- Reports ----------

    private static void displayReports() {
        System.out.println("\n========== WARD REPORT ==========");
        displayAllPatients();
        ward.displayAvailableBeds();
        ward.displayOccupiedBeds();
        System.out.println("\nTotal registered patients : " + patientManager.getTotalPatients());
        System.out.println("Total occupied beds       : " + ward.getOccupiedCount());
        System.out.printf("Ward occupancy percentage : %.1f%%%n", ward.getOccupancyPercentage());
    }

    private static void displaySorted() {
        System.out.println("Sort by: 1) Surname  2) Patient ID");
        int choice = readInt("Choose: ");
        List<Patient> sorted;
        if (choice == 1) {
            sorted = patientManager.getSortedBySurname();
        } else if (choice == 2) {
            sorted = patientManager.getSortedById();
        } else {
            System.out.println("Invalid choice.");
            return;
        }
        System.out.println("\n--- Sorted Patients ---");
        if (sorted.isEmpty()) {
            System.out.println("No patients registered.");
        }
        for (Patient p : sorted) {
            System.out.println(p.toSummaryLine());
        }
    }

    // ---------- Input helpers with exception handling ----------

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}