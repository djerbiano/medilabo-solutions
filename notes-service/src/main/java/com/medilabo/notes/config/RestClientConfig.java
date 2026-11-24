package com.medilabo.notes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient patientRestClient(@Value("${patient-service.url}") String patientServiceUrl,
                                        @Value("${patient-service.connect-timeout}") Duration connectTimeout,
                                        @Value("${patient-service.read-timeout}") Duration readTimeout) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        return RestClient.builder()
                .baseUrl(patientServiceUrl)
                .requestFactory(requestFactory)
                .build();
    }
}
