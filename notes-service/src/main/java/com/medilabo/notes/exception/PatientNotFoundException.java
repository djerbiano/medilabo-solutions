package com.medilabo.notes.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(Long patId) {
        super("Patient introuvable : " + patId);
    }
}