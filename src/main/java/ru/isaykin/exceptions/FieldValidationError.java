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

        switch (field) {
            case "firstName":
                result = "Please enter correct first name of author";
                break;
            case "lastName":
                result = "Please enter correct last name of author";
                break;
            case "birthdate":
                result = "Please enter correct birth date of author";
                break;

            default:
                result = "Please enter email in that form: myemailname@email.ru";
                break;
        }


        this.help = result;
    }

}
