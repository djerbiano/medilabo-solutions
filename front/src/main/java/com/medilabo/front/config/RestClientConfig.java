package com.medilabo.front.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient gatewayRestClient(@Value("${gateway.url}") String gatewayUrl,
                                        @Value("${gateway.username}") String username,
                                        @Value("${gateway.password}") String password) {
        return RestClient.builder()
                .baseUrl(gatewayUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }
}
