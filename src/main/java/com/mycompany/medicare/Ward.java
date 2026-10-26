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

public class Ward {
    public static final int WARD_NUMBER = 1;
    public static final int ROWS = 4;
    public static final int COLUMNS = 5;
    public static final int TOTAL_BEDS = ROWS * COLUMNS;

    private final Inpatient[][] beds = new Inpatient[ROWS][COLUMNS];

    /** Converts a row/column position into a bed code such as "B07". */
    public static String bedCode(int row, int col) {
        return String.format("B%02d", row * COLUMNS + col + 1);
    }

    /** Converts a bed code into {row, col}. Throws if the code is invalid. */
    private int[] locate(String bedCode) {
        if (bedCode == null) {
            throw new IllegalArgumentException("Bed number is required.");
        }
        String code = bedCode.trim().toUpperCase();
        for (int r = 0; r < beds.length; r++) {
            for (int c = 0; c < beds[r].length; c++) {
                if (bedCode(r, c).equals(code)) {
                    return new int[]{r, c};
                }
            }
        }
        throw new IllegalArgumentException("Invalid bed number: " + bedCode + ". Use B01 to B20.");
    }

    /** Allocates a specific bed to an inpatient. */
    public void allocateBed(Inpatient patient, String bedCode) {
        if (patient == null) {
            throw new IllegalArgumentException("Only inpatients can be allocated a bed.");
        }
        if (isFull()) {
            throw new IllegalStateException("No beds are available. The ward is full.");
        }
        if (patient.hasBed()) {
            throw new IllegalStateException("Patient " + patient.getPatientId()
                    + " already occupies bed " + patient.getBedNumber() + ".");
        }
        int[] pos = locate(bedCode);
        if (beds[pos[0]][pos[1]] != null) {
            throw new IllegalStateException("Bed " + bedCode.trim().toUpperCase() + " is already occupied.");
        }
        beds[pos[0]][pos[1]] = patient;
        patient.setWardNumber(WARD_NUMBER);
        patient.setBedNumber(bedCode(pos[0], pos[1]));
    }

    /** Allocates the first free bed to an inpatient and returns the bed code. */
    public String allocateNextAvailableBed(Inpatient patient) {
        List<String> free = getAvailableBeds();
        if (free.isEmpty()) {
            throw new IllegalStateException("No beds are available. The ward is full.");
        }
        allocateBed(patient, free.get(0));
        return free.get(0);
    }

    /** Releases a bed (patient discharged) and returns the inpatient who was in it. */
    public Inpatient releaseBed(String bedCode) {
        int[] pos = locate(bedCode);
        Inpatient patient = beds[pos[0]][pos[1]];
        if (patient == null) {
            throw new IllegalStateException("Bed " + bedCode.trim().toUpperCase() + " is not occupied.");
        }
        beds[pos[0]][pos[1]] = null;
        patient.clearBed();
        return patient;
    }

    public boolean isOccupied(String bedCode) {
        int[] pos = locate(bedCode);
        return beds[pos[0]][pos[1]] != null;
    }

    public boolean isFull() {
        return getOccupiedCount() == TOTAL_BEDS;
    }

    public int getOccupiedCount() {
        int count = 0;
        for (int r = 0; r < beds.length; r++) {
            for (int c = 0; c < beds[r].length; c++) {
                if (beds[r][c] != null) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getAvailableCount() {
        return TOTAL_BEDS - getOccupiedCount();
    }

    public double getOccupancyPercentage() {
        return (getOccupiedCount() * 100.0) / TOTAL_BEDS;
    }

    public List<String> getAvailableBeds() {
        List<String> list = new ArrayList<>();
        for (int r = 0; r < beds.length; r++) {
            for (int c = 0; c < beds[r].length; c++) {
                if (beds[r][c] == null) {
                    list.add(bedCode(r, c));
                }
            }
        }
        return list;
    }

    public List<Inpatient> getOccupyingPatients() {
        List<Inpatient> list = new ArrayList<>();
        for (Inpatient[] row : beds) {
            for (Inpatient p : row) {
                if (p != null) {
                    list.add(p);
                }
            }
        }
        return list;
    }

    /** Prints the full 4 x 5 layout. [ ] = available, [X] = occupied. */
    public void displayLayout() {
        System.out.println("\n--- Ward " + WARD_NUMBER + " Layout ([ ] available, [X] occupied) ---");
        for (int r = 0; r < beds.length; r++) {
            StringBuilder line = new StringBuilder();
            for (int c = 0; c < beds[r].length; c++) {
                line.append(bedCode(r, c)).append(beds[r][c] == null ? "[ ]" : "[X]").append("  ");
            }
            System.out.println(line.toString().trim());
        }
    }

    public void displayAvailableBeds() {
        List<String> free = getAvailableBeds();
        System.out.println("\n--- Available Beds (" + free.size() + ") ---");
        System.out.println(free.isEmpty() ? "No beds available." : String.join(", ", free));
    }

    public void displayOccupiedBeds() {
        List<Inpatient> occupied = getOccupyingPatients();
        System.out.println("\n--- Occupied Beds (" + occupied.size() + ") ---");
        if (occupied.isEmpty()) {
            System.out.println("No beds are occupied.");
            return;
        }
        for (Inpatient p : occupied) {
            System.out.println(p.getBedNumber() + " -> " + p.getPatientId() + " "
                    + p.getFirstName() + " " + p.getLastName());
        }
    }
}