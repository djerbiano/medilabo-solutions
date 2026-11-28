package com.medilabo.front.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record NoteDto(String id,

                      Long patId,

                      @NotBlank(message = "Le contenu de la note est obligatoire")
                      String content,

                      LocalDateTime createdAt) {
}
