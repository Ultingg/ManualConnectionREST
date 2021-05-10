package ru.isaykin.contextTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.isaykin.model.Author;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ValidationTests {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private Validator validator;



    @Test
    void validation_valid_validData() throws Exception {
        Author author = Author.builder()
                .firstName("Valera")
                .lastName("Tovarish")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1933-10-17"))
                .build();
        mvc.perform(post("/authors")
                .content(objectMapper.writeValueAsString(author))
                .contentType(APPLICATION_JSON));

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        assertTrue(violations.isEmpty());
    }
    @Test
    void validation_invalidFirstName_notValidData() throws Exception {
        Author author = Author.builder()
                .firstName("123")
                .lastName("Tovarish")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1933-10-17"))
                .build();
        mvc.perform(post("/authors")
                .content(objectMapper.writeValueAsString(author))
                .contentType(APPLICATION_JSON));

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        assertFalse(violations.isEmpty());
    }
    @Test
    void validation_invalidLastName_notValidData() throws Exception {
        Author author = Author.builder()
                .firstName("Valera")
                .lastName("T")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1933-10-17"))
                .build();
        mvc.perform(post("/authors")
                .content(objectMapper.writeValueAsString(author))
                .contentType(APPLICATION_JSON));

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        assertFalse(violations.isEmpty());
    }
    @Test
    void validation_invalidEmail_notValidData() throws Exception {
        Author author = Author.builder()
                .firstName("Valera")
                .lastName("T")
                .email("commraprofkom.ussr")
                .birthdate(LocalDate.parse("1933-10-17"))
                .build();
        mvc.perform(post("/authors")
                .content(objectMapper.writeValueAsString(author))
                .contentType(APPLICATION_JSON));

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        assertFalse(violations.isEmpty());
    }
    @Test
    void validation_invalidDate_notValidData() throws Exception {
        Author author = Author.builder()
                .firstName("Valera")
                .lastName("T")
                .email("commraprofkom.ussr")
                .birthdate(LocalDate.parse("1933-10-17"))
                .build();
        mvc.perform(post("/authors")
                .content(objectMapper.writeValueAsString(author))
                .contentType(APPLICATION_JSON));

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        assertFalse(violations.isEmpty());
    }
}
