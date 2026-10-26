/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.medicare;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 *
 * @author User
 */

class PatientManagerTest {
    private PatientManager manager;

    @BeforeEach
    void setUp() {
        manager = new PatientManager();
    }

    private Patient outpatient(String id, String first, String last) {
        return new Patient(id, first, last, 30, "Female", "Flu", PatientCategory.OUTPATIENT);
    }

    @Test
    void registerPatient_addsPatient() {
        manager.registerPatient(outpatient("P001", "Anna", "Smith"));
        assertEquals(1, manager.getTotalPatients());
    }

    @Test
    void searchPatient_findsExistingAndReturnsNullForMissing() {
        manager.registerPatient(outpatient("P001", "Anna", "Smith"));
        assertNotNull(manager.searchById("P001"));
        assertEquals("Anna", manager.searchById("p001").getFirstName());
        assertNull(manager.searchById("P999"));
    }

    @Test
    void updatePatient_changesDetails() {
        manager.registerPatient(outpatient("P001", "Anna", "Smith"));
        manager.updatePatient("P001", "Annie", "Jones", 41, "Female", "Asthma");
        Patient p = manager.searchById("P001");
        assertEquals("Annie", p.getFirstName());
        assertEquals("Jones", p.getLastName());
        assertEquals(41, p.getAge());
        assertEquals("Asthma", p.getMedicalCondition());
    }

    @Test
    void updatePatient_unknownIdThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> manager.updatePatient("P404", "A", "B", 20, "Male", "None"));
    }

    @Test
    void deletePatient_removesPatient() {
        manager.registerPatient(outpatient("P001", "Anna", "Smith"));
        assertTrue(manager.deletePatient("P001"));
        assertNull(manager.searchById("P001"));
        assertEquals(0, manager.getTotalPatients());
        assertFalse(manager.deletePatient("P001"));
    }

    @Test
    void registerPatient_preventsDuplicateIds() {
        manager.registerPatient(outpatient("P001", "Anna", "Smith"));
        assertThrows(IllegalArgumentException.class,
                () -> manager.registerPatient(outpatient("p001", "Bob", "Jones")));
        assertEquals(1, manager.getTotalPatients());
    }

    @Test
    void invalidAge_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Patient("P1", "A", "B", -5, "Male", "None", PatientCategory.EMERGENCY));
    }

    @Test
    void sortBySurname_ordersAlphabetically() {
        manager.registerPatient(outpatient("P003", "Cara", "Zulu"));
        manager.registerPatient(outpatient("P001", "Anna", "Naidoo"));
        manager.registerPatient(outpatient("P002", "Ben", "Adams"));
        List<Patient> sorted = manager.getSortedBySurname();
        assertEquals("Adams", sorted.get(0).getLastName());
        assertEquals("Naidoo", sorted.get(1).getLastName());
        assertEquals("Zulu", sorted.get(2).getLastName());
    }

    @Test
    void sortById_ordersByPatientId() {
        manager.registerPatient(outpatient("P003", "Cara", "Zulu"));
        manager.registerPatient(outpatient("P001", "Anna", "Naidoo"));
        manager.registerPatient(outpatient("P002", "Ben", "Adams"));
        List<Patient> sorted = manager.getSortedById();
        assertEquals("P001", sorted.get(0).getPatientId());
        assertEquals("P002", sorted.get(1).getPatientId());
        assertEquals("P003", sorted.get(2).getPatientId());
    }
}
