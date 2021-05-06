package ru.isaykin.exceptions;

import lombok.Data;

@Data
public class FieldValidationError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
    private String help;

    public FieldValidationError() {

    }

    public FieldValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public void helpSwitcher() {
        String result;

        if ("firstName".equals(field)) {
            result = "Please enter correct first name of author";
        } else if ("lastName".equals(field)) {
            result = "Please enter correct last name of author";
        } else if ("birthdate".equals(field)) {
            result = "Please enter correct birth date of author";
        } else {
            result = "Please enter email in that form: myemailname@email.ru";
        }


        this.help = result;
    }

}
