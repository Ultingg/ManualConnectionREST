package ru.isaykin.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.isaykin.reader.Author;
import ru.isaykin.repository.AuthorRepo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.now;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

class AuthorsSQLServiceTest {


    private AuthorRepo authorRepo;
    private AuthorsSQLService authorsSQLService;


    @Test
    void getById_validId_AuthorById() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        Author author = new Author();
        author.setId(1L);
        ResponseEntity<Author> expected = new ResponseEntity<>(author, OK);
        when(authorRepo.getById(1L)).thenReturn(author);

        ResponseEntity<Author> actual = authorsSQLService.getOneById(1L);

        assertEquals(expected, actual, "Get Author with id 1 by it's id");
        verify(authorRepo, times(1)).getById(1L);
        verify(authorRepo, times(1)).getById(anyLong());
    }

    @Test
    void getById_noExistedId_notFound() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        ResponseEntity<?> expected = new ResponseEntity<>(NOT_FOUND);

        ResponseEntity<Author> actual = authorsSQLService.getOneById(100L);

        assertEquals(expected, actual, "Get response when there is no author with that id");
        verify(authorRepo, times(1)).getById(100L);
        verify(authorRepo, times(1)).getById(anyLong());
    }

    @Test
    void getById_null_notFound() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        ResponseEntity<?> expected = new ResponseEntity<>(NOT_FOUND);

        ResponseEntity<Author> actual = authorsSQLService.getOneById(null);

        assertEquals(expected, actual, "Null-Id should get NOT_FOUND response");
        verify(authorRepo, times(1)).getById(null);
    }

    @Test
    void getAll_validResponse_allList() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        Author author = new Author();
        Author author1 = new Author();
        List<Author> authorList = Arrays.asList(author, author1);
        when(authorRepo.getAll()).thenReturn(authorList);

        List<Author> actual = authorsSQLService.getAll();

        assertEquals(actual, authorList, "List of all authors should be complete");
        verify(authorRepo, times(1)).getAll();
    }

    @Test
    void update_validId_validAuthorById() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        Author author = Author.builder()
                .id(1L)
                .firstName("Trevor")
                .lastName("Long")
                .email("longverylong@yahoo.com")
                .birthdate(LocalDate.of(1975, 5, 13))
                .build();
        when(authorRepo.getById(1L)).thenReturn(author);
        Author authorUpdated = Author.builder()
                .id(1L)
                .firstName("Trevor")
                .lastName("Long")
                .email("logan@bp.uk")
                .birthdate(LocalDate.of(1975, 5, 13))
                .build();
        Author expected = Author.builder()
                .id(1L)
                .firstName("Trevor")
                .lastName("Long")
                .email("logan@bp.uk")
                .birthdate(LocalDate.of(1975, 5, 13))
                .build();

        Author actual = authorsSQLService.update(1L, authorUpdated);

        assertEquals(expected, actual, "Checking if author was updated");
        verify(authorRepo, times(1)).save(authorUpdated);
        verify(authorRepo, times(1)).save(any());
        verify(authorRepo, times(1)).getById(anyLong());
        verify(authorRepo, times(1)).getById(1L);
    }

    @Test
    void update_nullAuthorFromRepo_null() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        when(authorRepo.getById(1L)).thenReturn(null);
        Author authorUpdated = Author.builder()
                .id(0L)
                .firstName("Trevor")
                .lastName("Logan")
                .email("logan@bp.uk")
                .birthdate(LocalDate.of(1975, 5, 13))
                .build();

        Author actual = authorsSQLService.update(1L, authorUpdated);

        assertNull(actual, "Checking if there is no author in empty List to update");
        verify(authorRepo, times(1)).getById(anyLong());
        verify(authorRepo, times(1)).getById(1L);
        verify(authorRepo, times(0)).save(authorUpdated);
        verify(authorRepo, times(0)).save(any());
    }

    @Test
    void delete_validId_OK() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        ResponseEntity<Author> expected = new ResponseEntity<>(OK);
        when(authorRepo.getById(1L)).thenReturn(new Author());

        ResponseEntity<Author> actual = authorsSQLService.delete(1L);

        assertEquals(expected, actual, "Checking that author was deleted, correct response");
        verify(authorRepo, times(1)).getById(anyLong());
        verify(authorRepo, times(1)).getById(1L);
        verify(authorRepo, times(1)).deleteById(anyLong());
        verify(authorRepo, times(1)).deleteById(1L);
    }

    @Test
    void delete_noExistedId_notFound() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        ResponseEntity<Author> expected = new ResponseEntity<>(NOT_FOUND);

        ResponseEntity<Author> actual = authorsSQLService.delete(1L);

        assertEquals(expected, actual, "Checking if author with wrong id wasn't found ");
        verify(authorRepo, times(1)).getById(anyLong());
        verify(authorRepo, times(1)).getById(1L);
        verify(authorRepo, times(0)).deleteById(anyLong());
        verify(authorRepo, times(0)).deleteById(1L);
    }

    @Test
    void delete_null_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        ResponseEntity<Author> expected = new ResponseEntity<>(NOT_FOUND);
        when(authorRepo.getById(null)).thenReturn(null);

        ResponseEntity<Author> actual = authorsSQLService.delete(null);

        assertEquals(expected, actual, "Checking if author with wrong id wasn't found ");
        verify(authorRepo, times(1)).getById(null);
        verify(authorRepo, times(0)).deleteById(null);
        verify(authorRepo, times(1)).getById(any());
        verify(authorRepo, times(0)).deleteById(any());
    }

    @Test
    void insert_validAuthor_validAuthor() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        Author author = Author.builder()
                .id(1L)
                .firstName("Yellow")
                .lastName("Car")
                .email("bumblebe@transformer.ab")
                .birthdate(LocalDate.parse("2020-10-12"))
                .build();
        when(authorRepo.getByEmail("bumblebe@transformer.ab")).
                thenReturn(author);

        Author expected = authorsSQLService.insert(author);

        assertEquals(expected, author, "Checking if the right author was inserted into list");
        verify(authorRepo, times(1))
                .insert(anyString(), anyString(), anyString(), any(Date.class));
        verify(authorRepo, times(1))
                .insert("Yellow", "Car", "bumblebe@transformer.ab", Date.valueOf(LocalDate.parse("2020-10-12")));
        verify(authorRepo, times(1)).getByEmail(anyString());
        verify(authorRepo, times(1)).getByEmail("bumblebe@transformer.ab");
    }

    @Test
    void insert_null_NPException() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);

        assertThrows(NullPointerException.class,
                () -> authorsSQLService.insert(null), "Checking nullPointerException was thrown");
    }

    @Test
    void insertMany_validListOfAuthors_validListOfAuthors() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        Author author = Author.builder()
                .id(1L)
                .firstName("Yellow")
                .lastName("Car")
                .email("bumblebe@transformer.ab")
                .birthdate(LocalDate.parse("2020-10-12"))
                .build();
        Author author1 = Author.builder()
                .id(1L)
                .firstName("Napoleon")
                .lastName("Bonaparte")
                .email("napoleon@imperor.fr")
                .birthdate(LocalDate.parse("1769-08-15"))
                .build();
        when(authorRepo.getByEmail("napoleon@imperor.fr")).thenReturn(author1);
        when(authorRepo.getByEmail("bumblebe@transformer.ab")).thenReturn(author);
        List<Author> expectedAuthorList = Arrays.asList(author, author1);

        List<Author> actual = authorsSQLService.insertMany(expectedAuthorList);

        assertEquals(expectedAuthorList, actual, "Checking if there were inserted right list of authors");
        verify(authorRepo, times(2)).getByEmail(any());
        verify(authorRepo, times(1)).getByEmail("bumblebe@transformer.ab");
        verify(authorRepo, times(1)).getByEmail("napoleon@imperor.fr");
        verify(authorRepo, times(2)).insert(anyString(), anyString(), anyString(), any(Date.class));
        verify(authorRepo, times(1)).insert("Yellow", "Car", "bumblebe@transformer.ab", Date.valueOf(LocalDate.parse("2020-10-12")));
        verify(authorRepo, times(1)).insert("Napoleon", "Bonaparte", "napoleon@imperor.fr", Date.valueOf(LocalDate.parse("1769-08-15")));

    }

    @Test
    void insertMany_null_NPException() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);

        assertThrows(NullPointerException.class,
                () -> authorsSQLService.insertMany(null), "Checking nullPointerException was thrown");
    }

    @Test
    @DisplayName("Test of get List of Greater then some age")
    void getGTAge_validAgeRange_validListOfAuthors() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);

        Date expected = Date.valueOf(now().minusYears(5));
        authorsSQLService.getListByAgeGT(5);

        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(expected);
    }

    @Test
    void getGTAge_null_NPException() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);

        assertThrows(NullPointerException.class, () -> authorsSQLService.getListByAgeGT(null));
    }

    @Test
    @DisplayName("Test of get List of Less then some age")
    void getLTAge_validAgeRange_validListOfAuthors() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);

        Date expected = Date.valueOf(now().minusYears(5));
        authorsSQLService.getListByAgeLT(5);

        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(expected);
    }

    @Test
    void getLTAge_null_NPException() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);

        assertThrows(NullPointerException.class, () -> authorsSQLService.getListByAgeLT(null));
    }

    @Nested
    final class ByFirstNameAndLastNameTests {
        Author author = Author.builder()
                .id(1L)
                .firstName("Yellow")
                .lastName("Car")
                .email("bumblebe@transformer.ab")
                .birthdate(LocalDate.parse("2020-10-12"))
                .build();
        Author author1 = Author.builder()
                .id(1L)
                .firstName("Napoleon")
                .lastName("Bonaparte")
                .email("napoleon@imperor.fr")
                .birthdate(LocalDate.parse("1769-08-15"))
                .build();
        Author author2 = Author.builder()
                .id(1L)
                .firstName("Friedrich")
                .lastName("Wilhelm II")
                .email("fridrich@imperor.pr")
                .birthdate(LocalDate.parse("1712-01-24"))
                .build();
        List<Author> authorList = Arrays.asList(author, author1, author2);

        @Test
        @DisplayName("getting List by FirstName")
        void getListByFirstName_validName_validListWithOneAuthor() {
            authorRepo = mock(AuthorRepo.class);
            authorsSQLService = new AuthorsSQLService(authorRepo);
            List<Author> expected = singletonList(author1);
            when(authorRepo.getAll()).thenReturn(authorList);

            List<Author> actual = authorsSQLService.getListByFirstNameAndLastNameOrNull("Napoleon", "Garic");

            assertEquals(expected, actual, "Checking searching by firstName");
            verify(authorRepo, times(1)).getAll();
        }

        @Test
        @DisplayName("getting List by LastName")
        void getListByLastName_validName_validListWithOneAuthor() {
            authorRepo = mock(AuthorRepo.class);
            authorsSQLService = new AuthorsSQLService(authorRepo);
            List<Author> expected = singletonList(author1);
            when(authorRepo.getAll()).thenReturn(authorList);

            List<Author> actual = authorsSQLService.getListByFirstNameAndLastNameOrNull("Romul", "Bonaparte");

            assertEquals(expected, actual, "Checking searching by LastName");
            verify(authorRepo, times(1)).getAll();
        }

        @Test
        @DisplayName("getting List by FirstName and LastName")
        void getListByFirstNameAndLastName_validNames_validListOfAuthors() {
            authorRepo = mock(AuthorRepo.class);
            authorsSQLService = new AuthorsSQLService(authorRepo);
            List<Author> expected = singletonList(author1);
            when(authorRepo.getAll()).thenReturn(authorList);

            List<Author> actual = authorsSQLService.getListByFirstNameAndLastNameOrNull("Napoleon", "Bonaparte");

            assertEquals(expected, actual, "Checking searching by FirstName and LastName");
            verify(authorRepo, times(1)).getAll();
        }

        @Test
        @DisplayName("getting List of few by FirstName and LastName")
        void getListByFirstNameAndLastNameFewAuthors_validNames_validListOfAuthors() {
            authorRepo = mock(AuthorRepo.class);
            authorsSQLService = new AuthorsSQLService(authorRepo);
            List<Author> authorList = Arrays.asList(author, author1, author2);
            List<Author> expectedList = Arrays.asList(author1, author2);
            when(authorRepo.getAll()).thenReturn(authorList);

            List<Author> actual4 = authorsSQLService.getListByFirstNameAndLastNameOrNull("Friedrich", "Bonaparte");

            assertEquals(expectedList, actual4, "Checking searching by firstName of List of two authors");
            verify(authorRepo, times(1)).getAll();
        }

        @Test
        void getListByFirstNameAndLastNameFewAuthors_notExistedNames_null() {
            authorRepo = mock(AuthorRepo.class);
            authorsSQLService = new AuthorsSQLService(authorRepo);
            List<Author> authorList = Arrays.asList(author, author1, author2);
            when(authorRepo.getAll()).thenReturn(authorList);

            List<Author> actual4 = authorsSQLService.getListByFirstNameAndLastNameOrNull("Molly", "Trevis");

            assertNull(actual4, "Checking if method returned null");
            verify(authorRepo, times(1)).getAll();
        }
    }

}