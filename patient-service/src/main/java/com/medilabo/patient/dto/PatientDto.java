package com.medilabo.patient.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientDto(
        Long id,
        @NotBlank(message = "Le prénom est obligatoire")
        @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
        String firstName,

        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
        String lastName,

        @NotNull(message = "La date de naissance est obligatoire")
        @PastOrPresent(message = "La date de naissance ne peut pas être dans le futur")
        LocalDate birthDate,

        @NotNull(message = "Le genre est obligatoire")
        Character gender,

        @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
        String address,

        @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
        String phone) {

    public PatientDto {
        if (firstName != null) {
            firstName = firstName.trim();
        }
        if (lastName != null) {
            lastName = lastName.trim();
        }
    }
}
