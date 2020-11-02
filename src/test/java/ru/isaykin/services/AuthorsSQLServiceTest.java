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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

class AuthorsSQLServiceTest {


    private AuthorRepo authorRepo;
    private AuthorsSQLService authorsSQLService;

    @Test
    void getById_valid_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        Author author = new Author();
        author.setId(1L);
        ResponseEntity<Author> expected = new ResponseEntity<Author>(author, OK);
        when(authorRepo.getById(1L)).thenReturn(author);

        ResponseEntity<Author> actual = authorsSQLService.getOneById(1L);

        assertEquals(expected, actual, "Get Author with id 1 by it's id");
        verify(authorRepo, times(1)).getById(1L);
        verify(authorRepo, times(1)).getById(anyLong());
    }

    @Test
    void getById_notFound_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        ResponseEntity<Author> expected = new ResponseEntity(NOT_FOUND);

        ResponseEntity<Author> actual = authorsSQLService.getOneById(100L);

        assertEquals(expected, actual, "Get response when there is no author with that id");
        verify(authorRepo, times(1)).getById(100L);
        verify(authorRepo, times(1)).getById(anyLong());
    }

    @Test
    void getById_null_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        ResponseEntity<Author> expected = new ResponseEntity(NOT_FOUND);

        ResponseEntity<Author> actual = authorsSQLService.getOneById(null);

        assertEquals(expected, actual, "Null-Id should get NOT_FOUND response");
        verify(authorRepo, times(1)).getById(null);
    }

    @Test
    void getAll_valid_success() {
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
    void update_valid_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Trevor");
        author.setLastName("Long");
        author.setBirthdate(LocalDate.of(1975, 05, 13));
        author.setEmail("longverylong@yahoo.com");
        when(authorRepo.getById(1L)).thenReturn(author);
        Author authorUpdated = new Author();
        authorUpdated.setEmail("logan@bp.uk");
        authorUpdated.setFirstName("Trevor");
        authorUpdated.setLastName("Logan");
        authorUpdated.setBirthdate(LocalDate.of(1975, 05, 13));
        Author expected = new Author();
        expected.setId(1L);
        expected.setBirthdate(LocalDate.of(1975, 05, 13));
        expected.setFirstName("Trevor");
        expected.setLastName("Logan");
        expected.setEmail("logan@bp.uk");

        Author actual = authorsSQLService.update(1L, authorUpdated);

        assertEquals(expected, actual, "Checking if author was updated");
        verify(authorRepo, times(1)).save(authorUpdated);
        verify(authorRepo, times(1)).save(any());
        verify(authorRepo, times(1)).getById(anyLong());
        verify(authorRepo, times(1)).getById(1L);
    }

    @Test
    void update_null_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        when(authorRepo.getById(1L)).thenReturn(null);
        Author authorUpdated = new Author();
        authorUpdated.setEmail("logan@bp.uk");
        authorUpdated.setFirstName("Trevor");
        authorUpdated.setLastName("Logan");
        authorUpdated.setBirthdate(LocalDate.of(1975, 05, 13));

        Author actual = authorsSQLService.update(1L, authorUpdated);

        assertEquals(null, actual, "Checking if there is no author in empty List to update");
        verify(authorRepo, times(1)).getById(anyLong());
        verify(authorRepo, times(1)).getById(1L);
        verify(authorRepo, times(0)).save(authorUpdated);
        verify(authorRepo, times(0)).save(any());
    }

    @Test
    void delete_valid_success() {
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
    void delete_notValid_success() {
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
//        ResponseEntity<Author> expected = new ResponseEntity<>(NOT_FOUND);
//
//        ResponseEntity<Author> actual = authorsSQLService.delete(null);

        assertThrows(NullPointerException.class,
                () -> authorsSQLService.delete(null));
//
//        assertEquals(expected, actual, "Checking if author with wrong id wasn't found ");
////        verify(authorRepo, times(1)).getById(anyLong());
        verify(authorRepo, times(1)).getById(null);
////        verify(authorRepo, times(0)).deleteById(anyLong());
        verify(authorRepo, times(0)).deleteById(null);
    }

    @Test
    void insert_valid_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        Author author = new Author();
        author.setEmail("bumblebe@transformer.ab");
        author.setFirstName("Yellow");
        author.setLastName("Car");
        author.setBirthdate(LocalDate.parse("2020-10-12"));
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
    void insert_null_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);

        assertThrows(NullPointerException.class,
                () -> authorsSQLService.insert(null), "Checking nullPointerException was thrown");
    }

    @Test
    void insertMany_valid_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        Author author = new Author();
        Author author1 = new Author();
        author.setEmail("bumblebe@transformer.ab");
        author.setFirstName("Yellow");
        author.setLastName("Car");
        author.setBirthdate(LocalDate.parse("2020-10-12"));
        author1.setEmail("napoleon@imperor.fr");
        author1.setFirstName("Napoleon");
        author1.setLastName("Bonaparte");
        author1.setBirthdate(LocalDate.parse("1769-08-15"));
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
    void insertMany_null_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);

        assertThrows(NullPointerException.class,
                () -> authorsSQLService.insertMany(null), "Checking nullPointerException was thrown");
    }

    @Test
    @DisplayName("Test of get List of Greater then some age")
    void getGTAge_valid_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);

        Date expected = Date.valueOf(now().minusYears(5));
        authorsSQLService.getListByAgeGT(5);

        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(expected);
    }

    @Test
    @DisplayName("Test of get List of Less then some age")
    void getLTAge_valid_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);

        Date expected = Date.valueOf(now().minusYears(5));
        authorsSQLService.getListByAgeLT(5);

        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(expected);
    }

    @Nested
    class ByFirstNameAndLastNameTests {
        Author author = new Author();
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author, author1, author2);

        {
            author.setEmail("bumblebe@transformer.ab");
            author.setFirstName("Yellow");
            author.setLastName("Car");
            author.setBirthdate(LocalDate.parse("2020-10-12"));
            author1.setEmail("napoleon@imperor.fr");
            author1.setFirstName("Napoleon");
            author1.setLastName("Bonaparte");
            author1.setBirthdate(LocalDate.parse("1769-08-15"));
            author2.setEmail("napoleon@imperor.fr");
            author2.setFirstName("Friedrich");
            author2.setLastName("Wilhelm II");
            author2.setBirthdate(LocalDate.parse("1712-01-24"));
        }

        @Test
        @DisplayName("getting List by FirstName")
        void getListByFirstName_valid_success() {
            authorRepo = mock(AuthorRepo.class);
            authorsSQLService = new AuthorsSQLService(authorRepo);
            List<Author> expected = Arrays.asList(author1);
            when(authorRepo.getAll()).thenReturn(authorList);

            List<Author> actual = authorsSQLService.getListByFirstNameAndLastName("Napoleon", "Garic");

            assertEquals(expected, actual, "Checking searching by firstName");
            verify(authorRepo, times(1)).getAll();
        }

        @Test
        @DisplayName("getting List by LastName")
        void getListByLastName_valid_success() {
            authorRepo = mock(AuthorRepo.class);
            authorsSQLService = new AuthorsSQLService(authorRepo);
            List<Author> expected = Arrays.asList(author1);
            when(authorRepo.getAll()).thenReturn(authorList);

            List<Author> actual = authorsSQLService.getListByFirstNameAndLastName("Romul", "Bonaparte");

            assertEquals(expected, actual, "Checking searching by LastName");
            verify(authorRepo, times(1)).getAll();
        }

        @Test
        @DisplayName("getting List by FirstName and LastName")
        void getListByFirstNameAndLastName_valid_success() {
            authorRepo = mock(AuthorRepo.class);
            authorsSQLService = new AuthorsSQLService(authorRepo);
            List<Author> expected = Arrays.asList(author1);
            when(authorRepo.getAll()).thenReturn(authorList);

            List<Author> actual = authorsSQLService.getListByFirstNameAndLastName("Napoleon", "Bonaparte");

            assertEquals(expected, actual, "Checking searching by FirstName and LastName");
            verify(authorRepo, times(1)).getAll();
        }

        @Test
        @DisplayName("getting List of few by FirstName and LastName")
        void getListByFirstNameAndLastNameFewAuthors_valid_success() {
            authorRepo = mock(AuthorRepo.class);
            authorsSQLService = new AuthorsSQLService(authorRepo);
            List<Author> authorList = Arrays.asList(author, author1, author2);
            List<Author> expectedList = Arrays.asList(author1, author2);
            when(authorRepo.getAll()).thenReturn(authorList);

            List<Author> actual4 = authorsSQLService.getListByFirstNameAndLastName("Friedrich", "Bonaparte");

            assertEquals(expectedList, actual4, "Checking searching by firstName of few authors");
            verify(authorRepo, times(1)).getAll();
        }

        @Test
        void getListByFirstNameAndLastNameFewAuthors_valid_success2() {
            authorRepo = mock(AuthorRepo.class);
            authorsSQLService = new AuthorsSQLService(authorRepo);
            List<Author> authorList = Arrays.asList(author, author1, author2);
            List<Author> expectedList = Arrays.asList(author1, author2);
            when(authorRepo.getAll()).thenReturn(authorList);

            List<Author> actual4 = authorsSQLService.getListByFirstNameAndLastName("LOh", "Pidr");
//            assertThrows(NullPointerException.class, () -> authorsSQLService.getListByFirstNameAndLastName("LOh", "Pidr"));
            assertEquals(null, actual4, "Checking searching by firstName of few authors");
            verify(authorRepo, times(1)).getAll();
        }
    }

}