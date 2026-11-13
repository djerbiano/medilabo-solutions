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
}
