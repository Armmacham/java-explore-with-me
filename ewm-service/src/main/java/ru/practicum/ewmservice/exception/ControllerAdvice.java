package ru.practicum.ewmservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    private ResponseEntity<ExceptionDto> handlePSQLException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(
                new ExceptionDto(ex.getMessage(), ex.getCause().getMessage(), HttpStatus.CONFLICT.name(), LocalDateTime.now()
        ), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    private ResponseEntity<ExceptionDto> handleException(Exception ex) {
        return new ResponseEntity<>(
                new ExceptionDto(ex.getMessage(), ex.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name(), LocalDateTime.now()
                ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
