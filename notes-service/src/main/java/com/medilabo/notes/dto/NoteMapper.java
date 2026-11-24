package com.medilabo.notes.dto;

import com.medilabo.notes.model.Note;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NoteMapper {
    public NoteDto toDto(Note note) {
        return new NoteDto(note.getId(), note.getPatId(), note.getContent(), note.getCreatedAt());
    }

    public Note toEntity(NoteDto dto) {
        return new Note(dto.patId(), dto.content(), LocalDateTime.now());
    }
}
