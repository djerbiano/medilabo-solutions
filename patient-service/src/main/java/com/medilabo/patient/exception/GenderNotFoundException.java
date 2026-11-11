package com.medilabo.patient.exception;

public class GenderNotFoundException extends RuntimeException {
    public GenderNotFoundException(Character code) {
        super("Genre inconnu : " + code);
    }
}
