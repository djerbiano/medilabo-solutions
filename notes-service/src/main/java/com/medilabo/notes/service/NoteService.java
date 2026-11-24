package com.medilabo.notes.service;

import com.medilabo.notes.client.PatientClient;
import com.medilabo.notes.dto.NoteDto;
import com.medilabo.notes.dto.NoteMapper;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final PatientClient patientClient;

    public List<NoteDto> findByPatId(Long patId) {
        patientClient.checkPatientExists(patId);
        return noteRepository.findByPatIdOrderByCreatedAtDesc(patId).stream()
                .map(note -> noteMapper.toDto(note))
                .toList();
    }

    public NoteDto create(NoteDto dto) {
        patientClient.checkPatientExists(dto.patId());
        Note saved = noteRepository.save(noteMapper.toEntity(dto));
        return noteMapper.toDto(saved);
    }
}
