package ru.isaykin.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.isaykin.reader.Author;
import ru.isaykin.services.AuthorsSQLService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

class AuthorControllerTest {


    AuthorsSQLService authorsSQLService;
    AuthorController authorController;

    @Test
    void getAuthor_valid_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Platon");
        author.setLastName("Swan");
        author.setEmail("swanoil@ug.ru");
        author.setBirthdate(LocalDate.of(1985,10,22));
        Author author1 = new Author();
        author1.setId(2L);
        author1.setFirstName("Lena");
        author1.setLastName("Puzzle");
        author1.setEmail("puzzle@mail.ru");
        author1.setBirthdate(LocalDate.of(1975,10,21));

        List<Author> authorList = Arrays.asList(author, author1);
        ResponseEntity<Object> expected = new ResponseEntity<>(Arrays.asList(author), OK);
        when(authorsSQLService.getListByFirstNameAndLastName("Platon", "Swan")).thenReturn(Arrays.asList(author));

        ResponseEntity<Object> actual = authorController.getListOrGetOneByFirstNameAndLastName("Platon", "Swan");

        assertEquals(expected, actual);
        verify(authorsSQLService, times(1)).getListByFirstNameAndLastName(anyString(), anyString());
        verify(authorsSQLService, times(1)).getListByFirstNameAndLastName("Platon", "Swan");
    }
}