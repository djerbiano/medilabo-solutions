package com.medilabo.patient.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientDto(
        Long id,
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @NotNull @Past LocalDate birthDate,
        @NotNull Character gender,
        @Size(max = 255) String address,
        @Size(max = 20) String phone) {
}
