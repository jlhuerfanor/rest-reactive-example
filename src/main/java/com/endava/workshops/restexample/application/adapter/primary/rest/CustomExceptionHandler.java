package com.endava.workshops.restexample.application.adapter.primary.rest;

import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidInputException.class)
    public Mono<ResponseEntity<Object>> handleInvalidInputException(
            InvalidInputException ex,
            ServerWebExchange exchange) {
        return super.handleExceptionInternal(ex, null, HttpHeaders.EMPTY,
                HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), exchange);
    }
}
