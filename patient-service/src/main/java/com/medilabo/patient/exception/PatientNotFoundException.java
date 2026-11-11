package com.medilabo.patient.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(Long id) {
        super("Patient introuvable : " + id);
    }
}
