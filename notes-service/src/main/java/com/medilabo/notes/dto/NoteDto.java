package com.medilabo.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NoteDto(
        String id,

        @NotNull(message = "L'identifiant du patient est obligatoire")
        Long patId,

        @NotBlank(message = "Le contenu de la note est obligatoire")
        String content,

        LocalDateTime createdAt) {

    public NoteDto {
        if (content != null) {
            content = content.trim();
        }
    }
}
