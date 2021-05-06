package ru.isaykin.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.isaykin.exceptions.AuthorNotFoundException;
import ru.isaykin.model.Author;
import ru.isaykin.repository.AuthorRepo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.now;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorsSQLServiceTest {


    private AuthorRepo authorRepo;
    private AuthorsSQLService authorsSQLService;

    @BeforeEach
    public void setUp() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
    }


    @Test
    void getById_validId_AuthorById() {
        Author expected = new Author();

        when(authorRepo.findById(1L)).thenReturn(java.util.Optional.of(expected));

        Author actual = authorsSQLService.getOneById(1L);

        assertEquals(expected, actual, "Get Author with id 1 by it's id");
        verify(authorRepo, times(1)).findById(1L);
        verify(authorRepo, times(1)).findById(anyLong());
    }

    @Test
    void getById_noExistedId_notFound() {
        assertThrows(AuthorNotFoundException.class, () -> authorsSQLService.getOneById(100L));
        verify(authorRepo, times(1)).findById(100L);
        verify(authorRepo, times(1)).findById(anyLong());
    }

    @Test
    void getById_null_notFound() {

        assertThrows(AuthorNotFoundException.class, () -> authorsSQLService.getOneById(null));
        verify(authorRepo, times(1)).findById(null);
    }

    @Test
    void getAll_validResponse_allList() {
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
        when(authorRepo.existsById(1L)).thenReturn(true);
        Author authorUpdated = Author.builder()
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
        when(authorRepo.save(any(Author.class))).thenReturn(expected);

        Author actual = authorsSQLService.update(1L, authorUpdated);

        assertEquals(expected, actual, "Checking if author was updated");
        verify(authorRepo, times(1)).save(authorUpdated);
        verify(authorRepo, times(1)).save(any());
        verify(authorRepo, times(1)).existsById(anyLong());
        verify(authorRepo, times(1)).existsById(1L);
    }

    @Test
    void update_nullAuthorFromRepo_AuthorNotFoundException() {
        when(authorRepo.existsById(1L)).thenReturn(false);

        assertThrows(AuthorNotFoundException.class, () -> authorsSQLService.update(1L, new Author()));
        verify(authorRepo, times(1)).existsById(anyLong());
        verify(authorRepo, times(1)).existsById(1L);
        verify(authorRepo, times(0)).save(any());
    }

    @Test
    void delete_validId_OK() {
        when(authorRepo.existsById(1L)).thenReturn(true);

        authorsSQLService.deleteById(1L);

        verify(authorRepo, times(1)).existsById(anyLong());
        verify(authorRepo, times(1)).existsById(1L);
        verify(authorRepo, times(1)).deleteById(anyLong());
        verify(authorRepo, times(1)).deleteById(1L);
    }

    @Test
    void delete_noExistedId_notFound() {

        assertThrows(AuthorNotFoundException.class, () -> authorsSQLService.deleteById(1L));
        verify(authorRepo, times(1)).existsById(anyLong());
        verify(authorRepo, times(1)).existsById(1L);
        verify(authorRepo, times(0)).deleteById(anyLong());
        verify(authorRepo, times(0)).deleteById(1L);
    }

    @Test
    void delete_null_success() {
        assertThrows(AuthorNotFoundException.class, () -> authorsSQLService.deleteById(null));
        verify(authorRepo, times(1)).existsById(null);
        verify(authorRepo, times(0)).deleteById(null);
    }

    @Test
    void insert_validAuthor_validAuthor() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Yellow")
                .lastName("Car")
                .email("bumblebe@transformer.ab")
                .birthdate(LocalDate.parse("2020-10-12"))
                .build();
        Author authorWithoutId = Author.builder()
                .firstName("Yellow")
                .lastName("Car")
                .email("bumblebe@transformer.ab")
                .birthdate(LocalDate.parse("2020-10-12"))
                .build();
        when(authorRepo.save(any(Author.class))).
                thenReturn(author);

        Author expected = authorsSQLService.insertAuthor(authorWithoutId);

        assertEquals(expected, author, "Checking if the right author was inserted into list");
        verify(authorRepo, times(1)).save(any(Author.class));
    }

    @Test
    void insertMany_validListOfAuthors_validListOfAuthors() {
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
        List<Author> authorList1 = Arrays.asList(authorExpected, authorExpected2);
        when(authorRepo.saveAll(anyIterable())).thenReturn(authorList1);
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

        List<Author> actual = authorsSQLService.insertMany(authorList);

        assertEquals(authorList1, actual, "Checking if there were inserted right list of authors");
        verify(authorRepo, times(1)).saveAll(anyIterable());
    }

    @Test
    @DisplayName("Test of get List of Greater then some age")
    void getGTAge_validAgeRange_validListOfAuthors() {
        Date expected = Date.valueOf(now().minusYears(5));
        authorsSQLService.getListByAgeGT(5);

        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(expected);
    }

    @Test
    void getGTAge_null_NPException() {

        assertThrows(NullPointerException.class, () -> authorsSQLService.getListByAgeGT(null));
    }

    @Test
    @DisplayName("Test of get List of Less then some age")
    void getLTAge_validAgeRange_validListOfAuthors() {
        Date expected = Date.valueOf(now().minusYears(5));
        authorsSQLService.getListByAgeLT(5);

        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(expected);
    }

    @Test
    void getLTAge_null_NPException() {

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
            List<Author> expected = singletonList(author1);
            when(authorRepo.findAll()).thenReturn(authorList);

            List<Author> actual = authorsSQLService.getListByFirstNameAndLastNameOrNull("Napoleon", "Garic");

            assertEquals(expected, actual, "Checking searching by firstName");
            verify(authorRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("getting List by LastName")
        void getListByLastName_validName_validListWithOneAuthor() {
            List<Author> expected = singletonList(author1);
            when(authorRepo.findAll()).thenReturn(authorList);

            List<Author> actual = authorsSQLService.getListByFirstNameAndLastNameOrNull("Romul", "Bonaparte");

            assertEquals(expected, actual, "Checking searching by LastName");
            verify(authorRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("getting List by FirstName and LastName")
        void getListByFirstNameAndLastName_validNames_validListOfAuthors() {
            List<Author> expected = singletonList(author1);
            when(authorRepo.findAll()).thenReturn(authorList);

            List<Author> actual = authorsSQLService.getListByFirstNameAndLastNameOrNull("Napoleon", "Bonaparte");

            assertEquals(expected, actual, "Checking searching by FirstName and LastName");
            verify(authorRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("getting List of few by FirstName and LastName")
        void getListByFirstNameAndLastNameFewAuthors_validNames_validListOfAuthors() {
            List<Author> authorList = Arrays.asList(author, author1, author2);
            List<Author> expectedList = Arrays.asList(author1, author2);
            when(authorRepo.findAll()).thenReturn(authorList);

            List<Author> actual4 = authorsSQLService.getListByFirstNameAndLastNameOrNull("Friedrich", "Bonaparte");

            assertEquals(expectedList, actual4, "Checking searching by firstName of List of two authors");
            verify(authorRepo, times(1)).findAll();
        }

        @Test
        void getListByFirstNameAndLastNameFewAuthors_notExistedNames_null() {
            List<Author> authorList = Arrays.asList(author, author1, author2);
            when(authorRepo.findAll()).thenReturn(authorList);

            List<Author> actual4 = authorsSQLService.getListByFirstNameAndLastNameOrNull("Molly", "Trevis");

            assertNull(actual4, "Checking if method returned null");
            verify(authorRepo, times(1)).findAll();
        }
    }

}