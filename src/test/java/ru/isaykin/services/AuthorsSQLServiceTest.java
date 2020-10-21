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

        assertEquals(expected, actual);
        verify(authorRepo, times(1)).getById(1L);
        verify(authorRepo, times(1)).getById(anyLong());
    }

    @Test
    void getById_null_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        ResponseEntity<Author> expected = new ResponseEntity(NOT_FOUND);

        ResponseEntity<Author> actual = authorsSQLService.getOneById(1L);

        assertEquals(expected, actual);
        verify(authorRepo, times(1)).getById(1L);
        verify(authorRepo, times(1)).getById(anyLong());
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

        assertEquals(actual, authorList);
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
        author.setBirthdate(LocalDate.of(1975,05,13));
        author.setEmail("longverylong@yahoo.com");
        when(authorRepo.getById(1L)).thenReturn(author);
        Author authorUpdated = new Author();
        authorUpdated.setEmail("logan@bp.uk");
        authorUpdated.setFirstName("Trevor");
        authorUpdated.setLastName("Logan");
        authorUpdated.setBirthdate(LocalDate.of(1975,05,13));
        Author expected = new Author();
        expected.setId(1L);
        expected.setBirthdate(LocalDate.of(1975,05,13));
        expected.setFirstName("Trevor");
        expected.setLastName("Logan");
        expected.setEmail("logan@bp.uk");

        Author actual = authorsSQLService.update(1L,authorUpdated);

        assertEquals(expected, actual);
        verify(authorRepo, times(1)).save(authorUpdated);
        verify(authorRepo, times(1)).save(any());
        verify(authorRepo,times(1)).getById(anyLong());
        verify(authorRepo,times(1)).getById(1L);
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
        authorUpdated.setBirthdate(LocalDate.of(1975,05,13));

        Author actual = authorsSQLService.update(1L, authorUpdated);

        assertEquals(null, actual);
        verify(authorRepo,times(1)).getById(anyLong());
        verify(authorRepo,times(1)).getById(1L);
    }

    @Test
    void delete_valid_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        ResponseEntity<Author> expected = new ResponseEntity<>(OK);
        when(authorRepo.getById(1L)).thenReturn(new Author());

        ResponseEntity<Author> actual = authorsSQLService.delete(1L);

        assertEquals(expected, actual);
        verify(authorRepo, times(1)).getById(anyLong());
        verify(authorRepo, times(1)).getById(1L);
        verify(authorRepo, times(1)).deleteById(anyLong());
        verify(authorRepo, times(1)).deleteById(1L);
    }
    @Test
    void delete_null_success() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        ResponseEntity<Author> expected = new ResponseEntity<>(NOT_FOUND);

        ResponseEntity<Author> actual = authorsSQLService.delete(1L);

        assertEquals(expected, actual);
        verify(authorRepo, times(1)).getById(anyLong());
        verify(authorRepo, times(1)).getById(1L);
    }

    @Test
    void insert_validInput_successOutput() {
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

        assertEquals(expected, author);
        verify(authorRepo, times(1))
                .insert(anyString(), anyString(), anyString(), any(Date.class));
        verify(authorRepo, times(1))
                .insert("Yellow", "Car", "bumblebe@transformer.ab", Date.valueOf(LocalDate.parse("2020-10-12")));
        verify(authorRepo, times(1)).getByEmail(anyString());
        verify(authorRepo, times(1)).getByEmail("bumblebe@transformer.ab");
    }

    @Test
    void insert_nullInput_successOutput() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
        assertThrows(NullPointerException.class,
                () -> authorsSQLService.insert(null));
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

        //TODO: NullPointer если не нашлось ни одного совпадения в базе.
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