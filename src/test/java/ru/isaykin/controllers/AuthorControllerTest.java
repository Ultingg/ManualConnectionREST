package ru.isaykin.controllers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.isaykin.reader.Author;
import ru.isaykin.services.AuthorsSQLService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Nested
    class ListOfAuthorsByAge {
        Author author = new Author();
        Author author1 = new Author();
        Author author2 = new Author();
        Author author3 = new Author();
        List<Author> authorListLT35;
        List<Author> authorListGT35;
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
            author1.setBirthdate(LocalDate.of(1975, 2, 21));
            author2.setId(3L);
            author2.setFirstName("Thomas");
            author2.setLastName("Milton");
            author2.setEmail("pubmuster@yahoo.com");
            author2.setBirthdate(LocalDate.of(1965, 5, 12));
            author3.setId(4L);
            author3.setFirstName("Norma");
            author3.setLastName("Price");
            author3.setEmail("privenorma@tulpan.com");
            author3.setBirthdate(LocalDate.of(1995, 12, 27));
            authorListLT35 = asList(author, author3);
            authorListGT35 = asList(author1, author2);

        }

        @Test
        void getListByAgeGT_valid_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(authorListGT35, OK);
            when(authorsSQLService.getListByAgeGT(35)).thenReturn(authorListGT35);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeGraterThen(35);

            assertEquals(expected, actual);
            verify(authorsSQLService, times(1)).getListByAgeGT(anyInt());
            verify(authorsSQLService, times(1)).getListByAgeGT(35);
        }

        @Test
        void getListByAgeGT_null_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            List<Author> authorListGT35 = new ArrayList<>();
            when(authorsSQLService.getListByAgeGT(35)).thenReturn(authorListGT35);
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(NOT_FOUND);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeGraterThen(35);

            assertEquals(expected, actual);
            verify(authorsSQLService, times(1)).getListByAgeGT(35);
            verify(authorsSQLService, times(1)).getListByAgeGT(anyInt());
        }

        @Test
        void getListByAgeLT_valid_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(authorListLT35, OK);
            when(authorsSQLService.getListByAgeLT(35)).thenReturn(authorListLT35);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeLessThen(35);

            assertEquals(expected, actual);
            verify(authorsSQLService, times(1)).getListByAgeLT(anyInt());
            verify(authorsSQLService, times(1)).getListByAgeLT(35);
        }
        @Test
        void getListByAgeLT_null_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            List<Author> authorListLT35 = new ArrayList<>();
            when(authorsSQLService.getListByAgeLT(35)).thenReturn(authorListLT35);
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(NOT_FOUND);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeLessThen(35);

            assertEquals(expected, actual);
            verify(authorsSQLService, times(1)).getListByAgeLT(anyInt());
            verify(authorsSQLService, times(1)).getListByAgeLT(35);
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
        ResponseEntity<Author> expected = new ResponseEntity<>(author, OK);

        ResponseEntity<Author> actual = authorController.insert(author);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(1)).insert(author);
        verify(authorsSQLService, times(1)).insert(any(Author.class));
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
    void createList_valid_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        Author author = new Author();
        Author author1 = new Author();
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
        List<Author> authorList = asList(author, author1);
        when(authorsSQLService.insertMany(authorList)).thenReturn(authorList);
        ResponseEntity<List<Author>> expected = new ResponseEntity<>(authorList, OK);

        ResponseEntity<List<Author>> actual = authorController.createList(authorList);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(1)).insertMany(anyList());
        verify(authorsSQLService, times(1)).insertMany(authorList);
    }

    @Test
    void crestList_null_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        List<Author> authorList = null;
        ResponseEntity<List<Author>> expected = new ResponseEntity<>(NO_CONTENT);

        ResponseEntity<List<Author>> actual = authorController.createList(authorList);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(0)).insertMany(anyList());
        verify(authorsSQLService, times(0)).insertMany(authorList);
    }

    @Test
    void delete_valid_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        when(authorsSQLService.delete(1L)).thenReturn(new ResponseEntity<>(OK));
        ResponseEntity<Author> expected = new ResponseEntity<>(OK);

        ResponseEntity<Author> actual = authorController.delete(1L);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(1)).delete(anyLong());
        verify(authorsSQLService, times(1)).delete(1L);
    }

    @Test
    void delete_null_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        when(authorsSQLService.delete(1L)).thenReturn(new ResponseEntity<>(NOT_FOUND));
        ResponseEntity<Author> expected = new ResponseEntity<>(NOT_FOUND);

        ResponseEntity<Author> actual = authorController.delete(1L);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(1)).delete(anyLong());
        verify(authorsSQLService, times(1)).delete(1L);
    }

    @Test
    void updateById_valid_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        Author author1 = new Author();
        author1.setFirstName("Pole");
        author1.setLastName("Swan");
        author1.setEmail("swanoil@ug.ru");
        author1.setBirthdate(LocalDate.of(1985, 10, 22));
        ResponseEntity<Author> expected = new ResponseEntity<Author>(author1, OK);
        when(authorsSQLService.update(1L, author1)).thenReturn(author1);

        ResponseEntity<Author> actual = authorController.updateById(1L, author1);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(1)).update(anyLong(), any(Author.class));
        verify(authorsSQLService, times(1)).update(1L, author1);
    }

    @Test
    void updateById_null_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        ResponseEntity<Author> expected = new ResponseEntity<>(NOT_MODIFIED);
        Author nullAuthor = null;
        Author author = new Author();
        author.setFirstName("Pole");
        author.setLastName("Swan");
        author.setEmail("swanoil@ug.ru");
        author.setBirthdate(LocalDate.of(1985, 10, 22));
        when(authorsSQLService.update(1L, author)).thenReturn(nullAuthor);

        ResponseEntity<Author> actual = authorController.updateById(1L, author);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(1)).update(anyLong(), any(Author.class));
        verify(authorsSQLService, times(1)).update(1L, author);
    }
}