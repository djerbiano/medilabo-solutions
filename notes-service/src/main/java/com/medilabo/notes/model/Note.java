package com.medilabo.notes.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notes")
@Getter
@Setter
@NoArgsConstructor
public class Note {
    @Id
    private String id;

    @NotNull
    @Indexed
    private Long patId;

    @NotBlank
    private String content;

    @NotNull
    private LocalDateTime createdAt;

    public Note(Long patId, String content, LocalDateTime createdAt) {
        this.patId = patId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
