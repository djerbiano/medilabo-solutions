package com.medilabo.front.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ResourceAccessException.class, HttpServerErrorException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleServiceUnavailable(Exception e) {
        log.warn("Service indisponible : {}", e.getMessage());
        return "error/service-unavailable";
    }
}
