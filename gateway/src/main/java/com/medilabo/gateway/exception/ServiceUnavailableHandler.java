package com.medilabo.gateway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

// Service arrêté ou injoignable : on répond 503 au lieu du 500 par défaut
@Component
@Order(-2) // passe avant le gestionnaire d'erreurs de Spring Boot
public class ServiceUnavailableHandler implements WebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ServiceUnavailableHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof ConnectException) {
            log.warn("Service injoignable : {}", ex.getMessage());
            return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE));
        }
        return Mono.error(ex);
    }
}
