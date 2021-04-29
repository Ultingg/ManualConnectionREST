package ru.isaykin.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class ApiError {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private  String help;
//    private String debugMessage;

    private List<FieldValidationError> fieldValidationErrors;



    ApiError(HttpStatus status, String message, Throwable ex) {
        this.status = status;
        this.message = message;
        timestamp = LocalDateTime.now();
    }


    void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(error -> {
            FieldValidationError subError = new FieldValidationError();
            subError.setField(error.getField());
            subError.setMessage(error.getDefaultMessage());
            subError.setRejectedValue(error.getRejectedValue());
            subError.setObject(error.getObjectName());
            subError.helpSwitcher();
            this.addSubError(subError);
        });
    }

    private void addSubError(FieldValidationError subError) {
        if (fieldValidationErrors == null) {
            fieldValidationErrors = new ArrayList<>();
        }
        fieldValidationErrors.add(subError);
    }

    public List<FieldValidationError> getFieldValidationErrors() {
        return fieldValidationErrors;
    }

    public void setFieldValidationErrors(List<FieldValidationError> apiValidationErrors) {
        this.fieldValidationErrors = apiValidationErrors;
    }
}
