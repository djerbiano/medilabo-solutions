package com.medilabo.notes.exception;

public class PatientServiceUnavailableException extends RuntimeException {
    public PatientServiceUnavailableException(Throwable cause) {
        super("Service patient indisponible, réessayez plus tard", cause);
    }
}