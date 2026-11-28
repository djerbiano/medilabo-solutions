package com.medilabo.front.client;

import com.medilabo.front.dto.NoteDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class NoteClient {
    private final RestClient restClient;

    public NoteClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<NoteDto> findByPatId(Long patId) {
        return restClient.get()
                .uri("/api/notes/patient/{patId}", patId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<NoteDto>>() {
                });
    }

    public NoteDto create(NoteDto noteDto) {
        return restClient.post()
                .uri("/api/notes")
                .body(noteDto)
                .retrieve()
                .body(NoteDto.class);
    }
}
