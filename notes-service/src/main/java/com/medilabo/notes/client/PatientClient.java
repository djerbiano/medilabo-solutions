package com.medilabo.notes.client;

import com.medilabo.notes.exception.PatientNotFoundException;
import com.medilabo.notes.exception.PatientServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
public class PatientClient {
    private final RestClient restClient;

    public void checkPatientExists(Long patId) {
        try {
            restClient.get()
                    .uri("/api/patients/{id}", patId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new PatientNotFoundException(patId);
        } catch (RestClientException e) {
            throw new PatientServiceUnavailableException(e);
        }
    }
}
