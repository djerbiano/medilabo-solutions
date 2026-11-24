package com.medilabo.notes.controller;

import com.medilabo.notes.dto.NoteDto;
import com.medilabo.notes.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/patient/{patId}")
    public List<NoteDto> findByPatId(@PathVariable Long patId) {
        return noteService.findByPatId(patId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteDto create(@Valid @RequestBody NoteDto dto) {
        return noteService.create(dto);
    }
}
