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
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class AuthorControllerTest {

    AuthorsSQLService authorsSQLService;
    AuthorController authorController;

    @Nested
    class getAuthorByFirstNameAndLastNameTests {

        Author author = Author.builder()
                .id(1L)
                .firstName("Platon")
                .lastName("Swan")
                .email("swanoil@ug.ru")
                .birthdate(LocalDate.of(1985, 10, 22))
                .build();
        Author author1 =  Author.builder()
                .id(2L)
                .firstName("Lena")
                .lastName("Puzzle")
                .email("puzzle@mail.ru")
                .birthdate(LocalDate.of(1975, 2, 21))
                .build();
        Author author2 =  Author.builder()
                .id(3L)
                .firstName("Muhamed")
                .lastName("Dzhabrailov")
                .email("muhamed97@mail.ru")
                .birthdate(LocalDate.of(1997, 5, 2))
                .build();

        @Test
        void getAuthor_valid_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<Object> expected = new ResponseEntity<>(singletonList(author), OK);
            when(authorsSQLService.getListByFirstNameAndLastNameOrNull("Platon", "Swan")).thenReturn(singletonList(author));

            ResponseEntity<Object> actualOneAuthor = authorController.getListOrGetOneByFirstNameAndLastName("Platon", "Swan");

            assertEquals(expected, actualOneAuthor, "Checking if correct Author was returned");
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull(anyString(), anyString());
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull("Platon", "Swan");
            verify(authorsSQLService, times(0)).getAll();
        }

        @Test
        void getAuthorTwoParameters_valid_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<Object> expectedTwo = new ResponseEntity<>(asList(author, author1), OK);
            when(authorsSQLService.getListByFirstNameAndLastNameOrNull("Platon", "Puzzle")).thenReturn(asList(author, author1));

            ResponseEntity<Object> actualTwoAuthors = authorController.getListOrGetOneByFirstNameAndLastName("Platon", "Puzzle");

            assertEquals(expectedTwo, actualTwoAuthors, "Checking if correct Authors were returned");
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull(anyString(), anyString());
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull("Platon", "Puzzle");
            verify(authorsSQLService, times(0)).getAll();
        }


        @Test
        void getListOfAuthors_listOfValidAuthors_listOfValidAuthors() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<Object> expectedList = new ResponseEntity<>(asList(author, author1, author2), OK);
            when(authorsSQLService.getAll()).thenReturn(asList(author, author1, author2));

            ResponseEntity<Object> actualList = authorController.getListOrGetOneByFirstNameAndLastName(null, null);

            assertEquals(expectedList, actualList, "Checking if correct List of Authors was returned");
            verify(authorsSQLService, times(1)).getAll();
            verify(authorsSQLService, times(0)).getListByFirstNameAndLastNameOrNull(anyString(), anyString());
        }

        @Test
        void getAuthor_null_notFound() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<Object> expected = new ResponseEntity<>(NOT_FOUND);
            when(authorsSQLService.getListByFirstNameAndLastNameOrNull("Stepan", "Fedorov")).thenReturn(null);

            ResponseEntity<Object> actual = authorController.getListOrGetOneByFirstNameAndLastName("Stepan", "Fedorov");

            assertEquals(expected, actual, "Checking if correct response (NOT FOUND) was returned");
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull(anyString(), anyString());
            verify(authorsSQLService, times(1)).getListByFirstNameAndLastNameOrNull("Stepan", "Fedorov");
            verify(authorsSQLService, times(0)).getAll();
        }
    }

    @Nested
    class ListOfAuthorsByAge {
        Author author = Author.builder()
                .id(1L)
                .firstName("Platon")
                .lastName("Swan")
                .email("swanoil@ug.ru")
                .birthdate(LocalDate.of(1985, 10, 22))
                .build();
        Author author1 =  Author.builder()
                .id(2L)
                .firstName("Lena")
                .lastName("Puzzle")
                .email("puzzle@mail.ru")
                .birthdate(LocalDate.of(1975, 2, 21))
                .build();
        Author author2 =  Author.builder()
                .id(3L)
                .firstName("Thomas")
                .lastName("Milton")
                .email("pubmuster@yahoo.com")
                .birthdate(LocalDate.of(1965, 5, 12))
                .build();
        Author author3 =  Author.builder()
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
        void getListByAgeGT_valid_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(authorListGT35, OK);
            when(authorsSQLService.getListByAgeGT(35)).thenReturn(authorListGT35);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeGraterThen(35);

            assertEquals(expected, actual, "Checking if correct List of Authors was returned");
            verify(authorsSQLService, times(1)).getListByAgeGT(anyInt());
            verify(authorsSQLService, times(1)).getListByAgeGT(35);
        }

        @Test
        void getListByAgeGT_noExistedAgeRange_notFound() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            List<Author> authorListGT35 = new ArrayList<>();
            when(authorsSQLService.getListByAgeGT(35)).thenReturn(authorListGT35);
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(NOT_FOUND);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeGraterThen(35);

            assertEquals(expected, actual, "Checking if correct response (NOT FOUND) was returned");
            verify(authorsSQLService, times(1)).getListByAgeGT(35);
            verify(authorsSQLService, times(1)).getListByAgeGT(anyInt());
        }

        @Test
        void getListByAgeGT_null_NPException() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            when(authorsSQLService.getListByAgeGT(null)).thenThrow(NullPointerException.class);
            assertThrows(NullPointerException.class, () -> authorController.getListByAgeGraterThen(null));
        }
        //TODO: getListByAgeGT null

        @Test
        void getListByAgeLT_valid_success() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(authorListLT35, OK);
            when(authorsSQLService.getListByAgeLT(35)).thenReturn(authorListLT35);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeLessThen(35);

            assertEquals(expected, actual, "Checking if correct List of Authors was returned");
            verify(authorsSQLService, times(1)).getListByAgeLT(anyInt());
            verify(authorsSQLService, times(1)).getListByAgeLT(35);
        }

        @Test
        void getListByAgeLT_noExistedAgeRange_notFound() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            List<Author> authorListLT35 = new ArrayList<>();
            when(authorsSQLService.getListByAgeLT(35)).thenReturn(authorListLT35);
            ResponseEntity<List<Author>> expected = new ResponseEntity<>(NOT_FOUND);

            ResponseEntity<List<Author>> actual = authorController.getListByAgeLessThen(35);

            assertEquals(expected, actual, "Checking if correct response (NOT FOUND) was returned");
            verify(authorsSQLService, times(1)).getListByAgeLT(anyInt());
            verify(authorsSQLService, times(1)).getListByAgeLT(35);
        }

        @Test
        void getListByAgeLT_null_NPException() {
            authorsSQLService = mock(AuthorsSQLService.class);
            authorController = new AuthorController(authorsSQLService);
            when(authorsSQLService.getListByAgeLT(null)).thenThrow(NullPointerException.class);
            assertThrows(NullPointerException.class, () -> authorController.getListByAgeLessThen(null));
        }
        //TODO: getListByAgeLT null
    }

    @Test
    void getAuthorById_valid_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        Author author = new Author();
        ResponseEntity<Author> expected = new ResponseEntity<>(author, OK);
        when(authorsSQLService.getOneById(1L)).thenReturn(new ResponseEntity<>(author, OK));

        ResponseEntity<Author> actual = authorController.getOneAuthor(1L);

        assertEquals(expected, actual, "Checking if correct response (OK) was returned and Author was found");
        verify(authorsSQLService, times(1)).getOneById(anyLong());
        verify(authorsSQLService, times(1)).getOneById(1L);
    }

    @Test
    void getAuthorById_noExistedId_notFound() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        ResponseEntity<Author> expected = new ResponseEntity<>(NOT_FOUND);
        when(authorsSQLService.getOneById(1L)).thenReturn(new ResponseEntity<>(NOT_FOUND));

        ResponseEntity<Author> actual = authorController.getOneAuthor(1L);

        assertEquals(expected, actual, "Checking if correct response (NOT FOUND) was returned");
        verify(authorsSQLService, times(1)).getOneById(anyLong());
        verify(authorsSQLService, times(1)).getOneById(1L);
    }
    //TODO: getAuthorByID null

    @Test
    void insert_valid_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        Author author = Author.builder()
                .id(1L)
                .firstName("Platon")
                .lastName("Swan")
                .email("swanoil@ug.ru")
                .birthdate(LocalDate.of(1985, 10, 22))
                .build();
        when(authorsSQLService.insert(author)).thenReturn(author);
        ResponseEntity<Author> expected = new ResponseEntity<>(author, OK);

        ResponseEntity<Author> actual = authorController.insert(author);

        assertEquals(expected, actual, "Checking if correct author was inserted");
        verify(authorsSQLService, times(1)).insert(author);
        verify(authorsSQLService, times(1)).insert(any(Author.class));
    }

    @Test
    void insert_null_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        ResponseEntity<Author> expected = new ResponseEntity<>(NO_CONTENT);

        ResponseEntity<Author> actual = authorController.insert(null);

        assertEquals(expected, actual, "Checking if correct response (NO CONTENT) returned with null ias parameter");
        verify(authorsSQLService, times(0)).insert(null);
        verify(authorsSQLService, times(0)).insert(any(Author.class));
    }


    @Test
    void createList_valid_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
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
                .birthdate(LocalDate.of(1975, 10, 21))
                .build();
        List<Author> authorList = asList(author, author1);
        when(authorsSQLService.insertMany(authorList)).thenReturn(authorList);
        ResponseEntity<List<Author>> expected = new ResponseEntity<>(authorList, OK);

        ResponseEntity<List<Author>> actual = authorController.createList(authorList);

        assertEquals(expected, actual, "Checking if correct List was created and correct response (OK)");
        verify(authorsSQLService, times(1)).insertMany(anyList());
        verify(authorsSQLService, times(1)).insertMany(authorList);
    }

    @Test
    void crestList_null_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        ResponseEntity<List<Author>> expected = new ResponseEntity<>(NO_CONTENT);

        ResponseEntity<List<Author>> actual = authorController.createList(null);

        assertEquals(expected, actual, "Checking if correct response (NO CONTENT) was returned with null as parameter");
        verify(authorsSQLService, times(0)).insertMany(anyList());
        verify(authorsSQLService, times(0)).insertMany(null);
    }

    @Test
    void delete_valid_success() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        when(authorsSQLService.delete(1L)).thenReturn(new ResponseEntity<>(OK));
        ResponseEntity<Author> expected = new ResponseEntity<>(OK);

        ResponseEntity<Author> actual = authorController.delete(1L);

        assertEquals(expected, actual, "Checking if correct response (OK) was returned and author was deleted");
        verify(authorsSQLService, times(1)).delete(anyLong());
        verify(authorsSQLService, times(1)).delete(1L);
    }

    @Test
    void delete_noExistedId_notFound() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        when(authorsSQLService.delete(1L)).thenReturn(new ResponseEntity<>(NOT_FOUND));
        ResponseEntity<Author> expected = new ResponseEntity<>(NOT_FOUND);

        ResponseEntity<Author> actual = authorController.delete(1L);

        assertEquals(expected, actual, "Checking if correct response (NOT FOUND) was returned if author wasn't found ");
        verify(authorsSQLService, times(1)).delete(anyLong());
        verify(authorsSQLService, times(1)).delete(1L);
    }

    @Test
    void delete_null_notFound() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        ResponseEntity<Author> expected = new ResponseEntity<>(NOT_FOUND);

        ResponseEntity<Author> actual = authorController.delete(null);

        assertEquals(expected, actual);
        verify(authorsSQLService, times(0)).delete(anyLong());
        verify(authorsSQLService, times(0)).delete(null);
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
        ResponseEntity<Author> expected = new ResponseEntity<>(author1, OK);
        when(authorsSQLService.update(1L, author1)).thenReturn(author1);

        ResponseEntity<Author> actual = authorController.updateById(1L, author1);

        assertEquals(expected, actual, "Checking if correct response (OK) was returned and author was updated");
        verify(authorsSQLService, times(1)).update(anyLong(), any(Author.class));
        verify(authorsSQLService, times(1)).update(1L, author1);
    }

    @Test
    void updateById_noExistedId_notFound() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
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