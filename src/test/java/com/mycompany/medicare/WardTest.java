/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.medicare;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author User
 */

class WardTest {
    private Ward ward;

    @BeforeEach
    void setUp() {
        ward = new Ward();
    }

    private Inpatient inpatient(int n) {
        return new Inpatient("P" + String.format("%03d", n), "First" + n, "Last" + n, 40, "Male", "Fracture");
    }

    @Test
    void allocateBed_assignsBedToInpatient() {
        Inpatient p = inpatient(1);
        ward.allocateBed(p, "B05");
        assertTrue(ward.isOccupied("B05"));
        assertEquals("B05", p.getBedNumber());
        assertEquals(Ward.WARD_NUMBER, p.getWardNumber());
        assertEquals(1, ward.getOccupiedCount());
    }

    @Test
    void releaseBed_freesBedAndClearsPatient() {
        Inpatient p = inpatient(1);
        ward.allocateBed(p, "B05");
        Inpatient released = ward.releaseBed("B05");
        assertSame(p, released);
        assertFalse(ward.isOccupied("B05"));
        assertFalse(p.hasBed());
        assertEquals(0, ward.getOccupiedCount());
    }

    @Test
    void allocateBed_preventsAllocatingOccupiedBed() {
        ward.allocateBed(inpatient(1), "B05");
        assertThrows(IllegalStateException.class, () -> ward.allocateBed(inpatient(2), "B05"));
    }

    @Test
    void allocateBed_preventsAllocationWhenWardIsFull() {
        for (int i = 1; i <= Ward.TOTAL_BEDS; i++) {
            ward.allocateNextAvailableBed(inpatient(i));
        }
        assertTrue(ward.isFull());
        assertEquals(100.0, ward.getOccupancyPercentage(), 0.001);
        assertThrows(IllegalStateException.class, () -> ward.allocateBed(inpatient(99), "B01"));
    }

    @Test
    void releaseBed_unoccupiedBedThrows() {
        assertThrows(IllegalStateException.class, () -> ward.releaseBed("B10"));
    }

    @Test
    void invalidBedNumber_throws() {
        assertThrows(IllegalArgumentException.class, () -> ward.allocateBed(inpatient(1), "B21"));
    }

    @Test
    void occupancyPercentage_isCorrect() {
        for (int i = 1; i <= 5; i++) {
            ward.allocateNextAvailableBed(inpatient(i));
        }
        assertEquals(25.0, ward.getOccupancyPercentage(), 0.001);
        assertEquals(15, ward.getAvailableBeds().size());
    }

    @Test
    void onlyInpatientsMayHaveBeds() {
        assertTrue(PatientCategory.INPATIENT.canBeAllocatedBed());
        assertFalse(PatientCategory.OUTPATIENT.canBeAllocatedBed());
        assertFalse(PatientCategory.EMERGENCY.canBeAllocatedBed());
    }
}
