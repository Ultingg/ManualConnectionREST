package ru.isaykin.controllers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.isaykin.reader.Author;
import ru.isaykin.services.AuthorsSQLService;

import java.time.LocalDate;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class AuthorControllerTest {


    AuthorsSQLService authorsSQLService;
    AuthorController authorController;

    @Nested
    class getAuthorByFirstNameAndLastNameTests {

        Author author = new Author();
        Author author1 = new Author();
        Author author2 = new Author();

        {
            author.setId(1L);
            author.setFirstName("Platon");
            author.setLastName("Swan");
            author.setEmail("swanoil@ug.ru");
            author.setBirthdate(LocalDate.of(1985, 10, 22));
            author1.setId(2L);
            author1.setFirstName("Lena");
            author1.setLastName("Puzzle");
            author1.setEmail("puzzle@mail.ru");
            author1.setBirthdate(LocalDate.of(1975, 10, 21));
            author2.setId(3L);
            author2.setFirstName("Muhamed");
            author2.setLastName("Dzhabrailov");
            author2.setEmail("muhamed97@mail.ru");
            author2.setBirthdate(LocalDate.of(1997, 05, 2));
        }

        @Test
        void getAuthor_valid_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<Object> expected = new ResponseEntity<>(asList(author), OK);
            when(authorsSQLService.getListByFirstNameAndLastName("Platon", "Swan")).thenReturn(asList(author));

            ResponseEntity<Object> actualOneAythor = authorController.getListOrGetOneByFirstNameAndLastName("Platon", "Swan");

            assertEquals(expected, actualOneAythor);
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastName(anyString(), anyString());
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastName("Platon", "Swan");
        }

        @Test
        void getAuthorTwoParameters_valid_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<Object> expectedTwo = new ResponseEntity<>(asList(author, author1), OK);
            when(authorsSQLService.getListByFirstNameAndLastName("Platon", "Puzzle")).thenReturn(asList(author, author1));

            ResponseEntity<Object> actualTwoAuthors = authorController.getListOrGetOneByFirstNameAndLastName("Platon", "Puzzle");

            assertEquals(expectedTwo, actualTwoAuthors);
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastName(anyString(), anyString());
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastName("Platon", "Puzzle");
        }

        @Test
        void getListOfAuthors_valid_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<Object> expectedList = new ResponseEntity<>(asList(author, author1, author2), OK);
            when(authorsSQLService.getAll()).thenReturn(asList(author, author1, author2));

            ResponseEntity<Object> actualList = authorController.getListOrGetOneByFirstNameAndLastName(null, null);

            assertEquals(expectedList, actualList);
            verify(authorsSQLService, times(1)).getAll();
        }
        @Test
        void getAuthor_null_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<Object> expected = new ResponseEntity<>(NOT_FOUND);
            when(authorsSQLService.getListByFirstNameAndLastName("Stepan", "Fedorov")).thenReturn(null);

            ResponseEntity<Object> actual = authorController.getListOrGetOneByFirstNameAndLastName("Stepan", "Fedorov");

            assertEquals(expected, actual);
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastName(anyString(), anyString());
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastName("Stepan", "Fedorov");
        }
    }

    @Test
    void getAuthorById_valid_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        ResponseEntity<Author> expected = new ResponseEntity<>(OK);
        when(authorsSQLService.getOneById(1L)).thenReturn(new ResponseEntity<>(OK));

        ResponseEntity<Author> actual = authorController.getOneAuthor(1L);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(1)).getOneById(anyLong());
        verify(authorsSQLService, times(1)).getOneById(1L);
    }
    @Test
    void getAuthorById_null_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        ResponseEntity<Author> expected = new ResponseEntity<>(NOT_FOUND);
        when(authorsSQLService.getOneById(1L)).thenReturn(new ResponseEntity<>(NOT_FOUND));

        ResponseEntity<Author> actual = authorController.getOneAuthor(1L);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(1)).getOneById(anyLong());
        verify(authorsSQLService, times(1)).getOneById(1L);
    }

    @Test
    void insert_null_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        Author authorNull = null;
        ResponseEntity<Author> expected = new ResponseEntity<>(NO_CONTENT);

        ResponseEntity<Author> actual = authorController.insert(authorNull);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(0)).insert(authorNull);
        verify(authorsSQLService, times(0)).insert(any(Author.class));
    }

    @Test
    void insert_valid_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Platon");
        author.setLastName("Swan");
        author.setEmail("swanoil@ug.ru");
        author.setBirthdate(LocalDate.of(1985, 10, 22));
        when(authorsSQLService.insert(author)).thenReturn(author);
        ResponseEntity<Author> expected = new ResponseEntity<>(author,OK);

        ResponseEntity<Author> actual = authorController.insert(author);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(1)).insert(author);
        verify(authorsSQLService, times(1)).insert(any(Author.class));
    }
}