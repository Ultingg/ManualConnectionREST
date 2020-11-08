package ru.isaykin.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class AuthorExceptionHendler {


    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleDuplicateException(SQLIntegrityConstraintViolationException exception, WebRequest request) {
        return new ResponseEntity<>(
                exception.getMessage(),INTERNAL_SERVER_ERROR);

    }
}
