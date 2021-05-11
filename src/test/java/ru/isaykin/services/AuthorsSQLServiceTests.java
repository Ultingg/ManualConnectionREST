package ru.isaykin.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.isaykin.exceptions.AuthorNotFoundException;
import ru.isaykin.exceptions.IllegalArgumentAuthorException;
import ru.isaykin.model.Author;
import ru.isaykin.repository.AuthorRepo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.now;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthorsSQLServiceTests {


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
    void insert_null_IllegalArgumentAuthorException() {
        assertThrows(IllegalArgumentAuthorException.class, () -> authorsSQLService.insertAuthor(null));
        verify(authorRepo, times(0)).save(any());
        verify(authorRepo, times(0)).save(null);
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
        List<Author> expected = Arrays.asList(new Author(), new Author());
        Date someDate = Date.valueOf(now().minusYears(5));
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(expected);

        List<Author> actual = authorsSQLService.getListByAgeGT(5);

        assertEquals(expected, actual);
        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(someDate);
    }

    @Test
    void getGTAge_null_IllegalArgumentAuthorException() {
        assertThrows(IllegalArgumentAuthorException.class, () -> authorsSQLService.getListByAgeGT(null));
        verify(authorRepo, times(0)).getListByAgeGraterThen(any(Date.class));
    }

    @Test
    void getGTAge_invalid_IllegalArgumentAuthorException() {
        assertThrows(IllegalArgumentAuthorException.class, () -> authorsSQLService.getListByAgeGT(-10));
        verify(authorRepo, times(0)).getListByAgeGraterThen(any(Date.class));
    }

    @Test
    @DisplayName("Test of get List of Less then some age")
    void getLTAge_validAgeRange_validListOfAuthors() {
        List<Author> expected = Arrays.asList(new Author(), new Author());
        Date someDate = Date.valueOf(now().minusYears(5));
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(expected);

        List<Author> actual = authorsSQLService.getListByAgeLT(5);

        assertEquals(expected, actual);
        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(someDate);
    }

    @Test
    void getLTAge_null_IllegalArgumentAuthorException() {
        assertThrows(IllegalArgumentAuthorException.class, () -> authorsSQLService.getListByAgeLT(null));
        verify(authorRepo, times(0)).getListByAgeLessThen(any(Date.class));
    }

    @Test
    void getLTAge_invalid_IllegalArgumentAuthorException() {
        assertThrows(IllegalArgumentAuthorException.class, () -> authorsSQLService.getListByAgeLT(-1));
        verify(authorRepo, times(0)).getListByAgeLessThen(any(Date.class));
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

            List<Author> actual = authorsSQLService.getListByFirstNameAndLastNameOrNull("Friedrich", "Bonaparte");

            assertEquals(expectedList, actual, "Checking searching by firstName of List of two authors");
            verify(authorRepo, times(1)).findAll();
        }

        @Test
        void getListByFirstNameAndLastNameFewAuthors_notExistedNames_AuthorNotFoundException() {
            List<Author> authorList = Arrays.asList(author, author1, author2);
            when(authorRepo.findAll()).thenReturn(authorList);

            assertThrows(AuthorNotFoundException.class, () -> authorsSQLService.getListByFirstNameAndLastNameOrNull("Molly", "Trevis")
                    , "Checking throwing of AuthorNotFoundException when searching not existing names.");
            verify(authorRepo, times(1)).findAll();
        }
    }

}