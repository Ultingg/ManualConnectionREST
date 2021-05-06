package ru.isaykin.exceptions;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class AuthorExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError(status, "method arg not valid", ex);
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleDuplicateException(SQLIntegrityConstraintViolationException exception, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", INTERNAL_SERVER_ERROR.toString());
        body.put("errors", exception.getMessage());


        return new ResponseEntity<>(
                body, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = AuthorNotFoundException.class)
    public ResponseEntity<Object> handleAuthorNotFoundException(AuthorNotFoundException exception) {
        return getResponseEntityWithBody(NOT_FOUND, exception);

    }

    private ResponseEntity<Object> getResponseEntityWithBody(HttpStatus status, Exception exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.toString());
        body.put("message", exception.getMessage());
        return new ResponseEntity<>(
                body, status);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleAuthorNotFoundException(IllegalArgumentException exception) {
        return getResponseEntityWithBody(BAD_REQUEST, exception);

    }


}
