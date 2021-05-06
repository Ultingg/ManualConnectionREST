package ru.isaykin.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.isaykin.model.Author;
import ru.isaykin.services.AuthorsSQLService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class AuthorControllerTest {

    AuthorsSQLService authorsSQLService;
    AuthorController authorController;


    @BeforeEach
    public void setUp(){
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
    }

    @Nested
    class getAuthorByFirstNameAndLastNameTests {

        Author author = Author.builder()
                .id(1L)
                .firstName("Platon")
                .lastName("Swan")
                .email("swanoil@ug.ru")
                .birthdate(LocalDate.of(1985, 10, 22))
                .build();
        Author author1 = Author.builder()
                .id(2L)
                .firstName("Lena")
                .lastName("Puzzle")
                .email("puzzle@mail.ru")
                .birthdate(LocalDate.of(1975, 2, 21))
                .build();
        Author author2 = Author.builder()
                .id(3L)
                .firstName("Muhamed")
                .lastName("Dzhabrailov")
                .email("muhamed97@mail.ru")
                .birthdate(LocalDate.of(1997, 5, 2))
                .build();

        @Test
        void getAuthor_validNames_validListOfAuthorsWithOneAuthorInIt() {
            ResponseEntity<Object> expected = new ResponseEntity<>(singletonList(author), OK);
            when(authorsSQLService.getListByFirstNameAndLastNameOrNull("Platon", "Swan")).thenReturn(singletonList(author));

            ResponseEntity<Object> actualOneAuthor = authorController.getListOrGetOneByFirstNameAndLastName("Platon", "Swan");

            assertEquals(expected, actualOneAuthor, "Checking if correct Author was returned");
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull(anyString(), anyString());
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull("Platon", "Swan");
            verify(authorsSQLService, times(0)).getAll();
        }

        @Test
        void getAuthorTwoParameters_validNames_validListOfAuthors() {
            ResponseEntity<?> expectedTwo = new ResponseEntity<>(asList(author, author1), OK);
            when(authorsSQLService.getListByFirstNameAndLastNameOrNull("Platon", "Puzzle")).thenReturn(asList(author, author1));

            ResponseEntity<Object> actualTwoAuthors = authorController.getListOrGetOneByFirstNameAndLastName("Platon", "Puzzle");

            assertEquals(expectedTwo, actualTwoAuthors, "Checking if correct Authors were returned");
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull(anyString(), anyString());
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull("Platon", "Puzzle");
            verify(authorsSQLService, times(0)).getAll();
        }


        @Test
        void getListOfAuthors_listOfValidAuthors_listOfValidAuthors() {
            ResponseEntity<?> expectedList = new ResponseEntity<>(asList(author, author1, author2), OK);
            when(authorsSQLService.getAll()).thenReturn(asList(author, author1, author2));

            ResponseEntity<Object> actualList = authorController.getListOrGetOneByFirstNameAndLastName(null, null);

            assertEquals(expectedList, actualList, "Checking if correct List of Authors was returned");
            verify(authorsSQLService, times(1)).getAll();
            verify(authorsSQLService, times(0)).getListByFirstNameAndLastNameOrNull(anyString(), anyString());
        }

        @Test
        void getAuthor_null_notFound() {
            ResponseEntity<?> expected = new ResponseEntity<>(NOT_FOUND);
            when(authorsSQLService.getListByFirstNameAndLastNameOrNull("Stepan", "Fedorov")).thenReturn(null);

            ResponseEntity<Object> actual = authorController.getListOrGetOneByFirstNameAndLastName("Stepan", "Fedorov");

            assertEquals(expected, actual, "Checking if correct response (NOT FOUND) was returned");
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull(anyString(), anyString());
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull("Stepan", "Fedorov");
            verify(authorsSQLService, times(0)).getAll();
        }
    }

    @SuppressWarnings("CanBeFinal")
    @Nested
    class ListOfAuthorsByAge {
        Author author = Author.builder()
                .id(1L)
                .firstName("Platon")
                .lastName("Swan")
                .email("swanoil@ug.ru")
                .birthdate(LocalDate.of(1985, 10, 22))
                .build();
        Author author1 = Author.builder()
                .id(2L)
                .firstName("Lena")
                .lastName("Puzzle")
                .email("puzzle@mail.ru")
                .birthdate(LocalDate.of(1975, 2, 21))
                .build();
        Author author2 = Author.builder()
                .id(3L)
                .firstName("Thomas")
                .lastName("Milton")
                .email("pubmuster@yahoo.com")
                .birthdate(LocalDate.of(1965, 5, 12))
                .build();
        Author author3 = Author.builder()
                .id(4L)
                .firstName("Norma")
                .lastName("Price")
                .email("privenorma@tulpan.com")
                .birthdate(LocalDate.of(1995, 12, 27))
                .build();
        List<Author> authorListLT35;
        List<Author> authorListGT35;

        {
            authorListLT35 = asList(author, author3);
            authorListGT35 = asList(author1, author2);

        }

        @Test
        void getListByAgeGT_validAge_validListOfAuthors() {
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(authorListGT35, OK);
            when(authorsSQLService.getListByAgeGT(35)).thenReturn(authorListGT35);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeGraterThen(35);

            assertEquals(expected, actual, "Checking if correct List of Authors was returned");
            verify(authorsSQLService, times(1)).getListByAgeGT(anyInt());
            verify(authorsSQLService, times(1)).getListByAgeGT(35);
        }

        @Test
        void getListByAgeGT_noExistedAgeRange_notFound() {
            List<Author> authorListGT35 = new ArrayList<>();
            when(authorsSQLService.getListByAgeGT(35)).thenReturn(authorListGT35);
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(NOT_FOUND);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeGraterThen(35);

            assertEquals(expected, actual, "Checking if correct response (NOT FOUND) was returned");
            verify(authorsSQLService, times(1)).getListByAgeGT(35);
            verify(authorsSQLService, times(1)).getListByAgeGT(anyInt());
        }

        @Test
        void getListByAgeGT_null_notFound() {
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(NOT_FOUND);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeGraterThen(null);

            assertEquals(expected, actual, "Checking correct response (NOT FOUND) for null as parameter");
            verify(authorsSQLService,times(0)).getListByAgeGT(any());
            verify(authorsSQLService,times(0)).getListByAgeGT(null);
        }

        @Test
        void getListByAgeLT_validAge_validListOfAuthors() {
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(authorListLT35, OK);
            when(authorsSQLService.getListByAgeLT(35)).thenReturn(authorListLT35);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeLessThen(35);

            assertEquals(expected, actual, "Checking if correct List of Authors was returned");
            verify(authorsSQLService, times(1)).getListByAgeLT(anyInt());
            verify(authorsSQLService, times(1)).getListByAgeLT(35);
        }

        @Test
        void getListByAgeLT_noExistedAgeRange_notFound() {
            List<Author> authorListLT35 = new ArrayList<>();
            when(authorsSQLService.getListByAgeLT(35)).thenReturn(authorListLT35);
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(NOT_FOUND);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeLessThen(35);

            assertEquals(expected, actual, "Checking if correct response (NOT FOUND) was returned");
            verify(authorsSQLService, times(1)).getListByAgeLT(anyInt());
            verify(authorsSQLService, times(1)).getListByAgeLT(35);
        }

        @Test
        void getListByAgeLT_null_notFound() {
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(NOT_FOUND);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeLessThen(null);

            assertEquals(expected, actual, "Checking correct response (NOT FOUND) for null as parameter");
            verify(authorsSQLService, times(0)).getListByAgeGT(any());
            verify(authorsSQLService, times(0)).getListByAgeLT(null);
        }
    }

    @Test
    void getAuthorById_validId_validAuthorOK() {
        Author author = new Author();
        ResponseEntity<Author> expected = new ResponseEntity<>(author, OK);
        when(authorsSQLService.getOneById(1L)).thenReturn(author);

        ResponseEntity<Author> actual = authorController.getOneAuthor(1L);

        assertEquals(expected, actual, "Checking if correct response (OK) was returned and Author was found");
        verify(authorsSQLService, times(1)).getOneById(anyLong());
        verify(authorsSQLService, times(1)).getOneById(1L);
    }

    @Test
    void insert_validAuthor_validAuthorOK() {

        Author author = Author.builder()
                .id(1L)
                .firstName("Platon")
                .lastName("Swan")
                .email("swanoil@ug.ru")
                .birthdate(LocalDate.of(1985, 10, 22))
                .build();
        when(authorsSQLService.insertAuthor(author)).thenReturn(author);
        ResponseEntity<Author> expected = new ResponseEntity<>(author, OK);

        ResponseEntity<Author> actual = authorController.insert(author);

        assertEquals(expected, actual, "Checking if correct author was inserted");
        verify(authorsSQLService, times(1)).insertAuthor(author);
        verify(authorsSQLService, times(1)).insertAuthor(any(Author.class));
    }

    @Test
    void insert_nullId_noContent() {
        ResponseEntity<Author> expected = new ResponseEntity<>(NO_CONTENT);

        ResponseEntity<Author> actual = authorController.insert(null);

        assertEquals(expected, actual, "Checking if correct response (NO CONTENT) returned with null as parameter");
        verify(authorsSQLService, times(0)).insertAuthor(null);
        verify(authorsSQLService, times(0)).insertAuthor(any(Author.class));
    }


    @Test
    void insertList_validListOfAuthors_validListOfAuthors() {
        Author authorExpected = Author.builder()
                .id(1L)
                .firstName("Yellow")
                .lastName("Car")
                .email("bumblebe@transformer.ab")
                .birthdate(LocalDate.parse("2020-10-12"))
                .build();
        Author authorExpected2 = Author.builder()
                .id(1L)
                .firstName("Napoleon")
                .lastName("Bonaparte")
                .email("napoleon@imperor.fr")
                .birthdate(LocalDate.parse("1769-08-15"))
                .build();
        List<Author> authorListExpected = Arrays.asList(authorExpected, authorExpected2);
        Author author = Author.builder()
                .firstName("Yellow")
                .lastName("Car")
                .email("bumblebe@transformer.ab")
                .birthdate(LocalDate.parse("2020-10-12"))
                .build();
        Author author1 = Author.builder()
                .firstName("Napoleon")
                .lastName("Bonaparte")
                .email("napoleon@imperor.fr")
                .birthdate(LocalDate.parse("1769-08-15"))
                .build();
        List<Author> authorList = Arrays.asList(author, author1);
        when(authorsSQLService.insertMany(authorList)).thenReturn(authorListExpected);
        ResponseEntity<List<Author>> expected = new ResponseEntity<>(authorListExpected, OK);

        ResponseEntity<List<Author>> actual = authorController.insertList(authorList);

        assertEquals(expected, actual, "Checking if correct List was created and correct response (OK)");
        verify(authorsSQLService, times(1)).insertMany(any());
        verify(authorsSQLService, times(1)).insertMany(authorList);
    }


    @Test
    void delete_validId_NO_CONTENT() {
        ResponseEntity<Author> expected = new ResponseEntity<>(NO_CONTENT);

        ResponseEntity<Author> actual = authorController.delete(1L);

        assertEquals(expected, actual, "Checking if correct response (NO_CONTENT) was returned and author was deleted");
        verify(authorsSQLService, times(1)).deleteById(anyLong());
        verify(authorsSQLService, times(1)).deleteById(1L);
    }

    @Test
    void updateById_validId_validAuthorOK() {
        Author author1 = new Author();
        author1.setFirstName("Pole");
        author1.setLastName("Swan");
        author1.setEmail("swanoil@ug.ru");
        author1.setBirthdate(LocalDate.of(1985, 10, 22));
        ResponseEntity<Author> expected = new ResponseEntity<>(author1, OK);
        when(authorsSQLService.update(1L, author1)).thenReturn(author1);

        ResponseEntity<Author> actual = authorController.updateById(1L, author1);

        assertEquals(expected, actual, "Checking if correct response (OK) was returned and author was updated");
        verify(authorsSQLService, times(1)).update(anyLong(), any(Author.class));
        verify(authorsSQLService, times(1)).update(1L, author1);
    }

    @Test
    void updateById_noExistedId_notFound() {
        ResponseEntity<Author> expected = new ResponseEntity<>(NOT_MODIFIED);
        Author author = new Author();
        author.setFirstName("Pole");
        author.setLastName("Swan");
        author.setEmail("swanoil@ug.ru");
        author.setBirthdate(LocalDate.of(1985, 10, 22));
        when(authorsSQLService.update(1L, author)).thenReturn(null);

        ResponseEntity<Author> actual = authorController.updateById(1L, author);

        assertEquals(expected, actual, "Checking if correct response (NOT MODIFIED) was returned id author wasn't found in List");
        verify(authorsSQLService, times(1)).update(anyLong(), any(Author.class));
        verify(authorsSQLService, times(1)).update(1L, author);
    }

}