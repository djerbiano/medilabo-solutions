package com.medilabo.front.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient gatewayRestClient(@Value("${gateway.url}") String gatewayUrl,
                                        @Value("${gateway.username}") String username,
                                        @Value("${gateway.password}") String password,
                                        @Value("${gateway.connect-timeout}") Duration connectTimeout,
                                        @Value("${gateway.read-timeout}") Duration readTimeout) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        return RestClient.builder()
                .baseUrl(gatewayUrl)
                .requestFactory(requestFactory)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }
}
