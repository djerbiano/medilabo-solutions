package com.medilabo.front.dto;

import java.time.LocalDate;

public record PatientDto(Long id,
                         String firstName,
                         String lastName,
                         LocalDate birthDate,
                         Character gender,
                         String address,
                         String phone) {
}
