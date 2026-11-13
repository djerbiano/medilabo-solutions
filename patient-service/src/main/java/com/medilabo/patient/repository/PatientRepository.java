package com.medilabo.patient.repository;


import com.medilabo.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByFirstNameAndLastNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

    boolean existsByFirstNameAndLastNameAndBirthDateAndIdNot(String firstName, String lastName, LocalDate birthDate, Long id);
}
