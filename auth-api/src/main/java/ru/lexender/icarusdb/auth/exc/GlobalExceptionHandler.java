package ru.lexender.icarusdb.auth.exc;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, List<String>>> handleWebChangeBindException(WebExchangeBindException ex) {
        return ResponseEntity.badRequest().body(ex.getFieldErrors().stream()
                .collect(Collectors.groupingBy(fieldError -> toSnakeCase(fieldError.getField()),
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList()))));
    }

    private String toSnakeCase(String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}

