package ru.isaykin.exceptions;


import org.springframework.data.relational.core.conversion.DbActionExecutionException;
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

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class AuthorExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError(status, "method arg not valid", ex);
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DbActionExecutionException.class)
    public ResponseEntity<Object> handleDuplicateException(DbActionExecutionException exception, WebRequest request) {
        SQLIntegrityConstraintViolationException sqlException = unwrapCause(SQLIntegrityConstraintViolationException.class, exception);
            return getResponseEntityWithBody(BAD_REQUEST, sqlException);
    }
    private static <T> T unwrapCause(Class<T> clazz, Throwable e) {
        while (!clazz.isInstance(e) && e.getCause() != null && e != e.getCause()) {
            e = e.getCause();
        }
        return clazz.isInstance(e) ? clazz.cast(e) : null;
    }
    private ResponseEntity<Object> getResponseEntityWithBody(HttpStatus status, Exception exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.toString());
        body.put("message", exception.getMessage());
        return new ResponseEntity<>(
                body, status);
    }

    @ExceptionHandler(value = AuthorNotFoundException.class)
    public ResponseEntity<Object> handleAuthorNotFoundException(AuthorNotFoundException exception) {
        return getResponseEntityWithBody(NOT_FOUND, exception);

    }

    @ExceptionHandler(value = IllegalArgumentAuthorException.class)
    public ResponseEntity<Object> handleAuthorNotFoundException(IllegalArgumentAuthorException exception) {
        return getResponseEntityWithBody(BAD_REQUEST, exception);

    }



}
