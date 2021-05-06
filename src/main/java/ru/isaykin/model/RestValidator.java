package ru.isaykin.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component("beforeCreateAuthorValidator")
public class RestValidator implements Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        return Author.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Author author = (Author) o;
        if (checkInputString(author.getFirstName())) {
            errors.rejectValue("firstName", "firstName is empty");
        }
        if (checkInputString(author.getLastName())) {
            errors.rejectValue("lastName", "lastName is empty");
        }

    }


    private boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }
}
