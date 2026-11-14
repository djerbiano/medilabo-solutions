package com.medilabo.front.client;

import com.medilabo.front.dto.PatientDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class PatientClient {
    private final RestClient restClient;

    public PatientClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<PatientDto> findAll() {
        return restClient.get()
                .uri("/api/patients")
                .retrieve()
                .body(new ParameterizedTypeReference<List<PatientDto>>() {
                });
    }

    public PatientDto create(PatientDto patientDto) {
        return restClient.post()
                .uri("/api/patients")
                .body(patientDto)
                .retrieve()
                .body(PatientDto.class);
    }

    public PatientDto findById(Long id) {
        return restClient.get()
                .uri("/api/patients/{id}", id)
                .retrieve()
                .body(PatientDto.class);
    }

    public PatientDto update(Long id, PatientDto patientDto) {
        return restClient.put()
                .uri("/api/patients/{id}", id)
                .body(patientDto)
                .retrieve()
                .body(PatientDto.class);
    }
}
