package ru.lexender.icarusdb.struct.exc;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IOException.class)
    public Mono<ResponseEntity<Object>> handleIOException(IOException ex, WebRequest request) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("IOException"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Object>> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                       WebRequest request) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<Object>> handleConstraintViolationException(ConstraintViolationException ex,
                                                                           WebRequest request) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(cv -> cv.getPropertyPath().toString(), ConstraintViolation::getMessage));
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Object>> handleException(Exception ex,
                                                        WebRequest request) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error on serverside"));
    }

}
