package com.medilabo.patient.exception;

import java.time.LocalDate;

public class PatientAlreadyExistsException extends RuntimeException {
    public PatientAlreadyExistsException(String firstName, String lastName, LocalDate birthDate) {
        super("Un patient existe déjà : " + firstName + " " + lastName + " né(e) le " + birthDate);
    }
}
