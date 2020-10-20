package ru.isaykin.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.isaykin.reader.Author;
import ru.isaykin.repository.AuthorRepo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorsSQLServiceTest {


    private AuthorRepo authorRepo;
    private AuthorsSQLService authorsSQLService;

    @BeforeEach
    void setUp() {
        authorRepo = mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
    }


    @Test
    void getAll() {

        Author author = new Author();
        List<Author> authorList = Arrays.asList(author);
        when(authorRepo.getAll()).thenReturn(authorList);
        List<Author> expected = authorRepo.getAll();
        assertEquals(expected, authorList);
    }

    @Test
    void insert_validInput_successOutput() {
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
        assertThrows(NullPointerException.class,
                () -> authorsSQLService.insert(null));
    }

    @Test
    void getListByFirstNameAndLastName() {
        Author author = new Author();
        author.setEmail("bumblebe@transformer.ab");
        author.setFirstName("Yellow");
        author.setLastName("Car");
        author.setBirthdate(LocalDate.parse("2020-10-12"));

        Author author1 = new Author();
        author1.setEmail("napoleon@imperor.fr");
        author1.setFirstName("Napoleon");
        author1.setLastName("Bonaparte");
        author1.setBirthdate(LocalDate.parse("1769-08-15"));

        Author author2 = new Author();
        author2.setEmail("napoleon@imperor.fr");
        author2.setFirstName("Friedrich");
        author2.setLastName("Wilhelm II");
        author2.setBirthdate(LocalDate.parse("1712-01-24"));

        List<Author> authorList = Arrays.asList(author, author1, author2);

        when(authorRepo.getAll()).thenReturn(authorList);

        List<Author> expected = Arrays.asList(author1);
        List<Author> expectedList = Arrays.asList(author1, author2);


        List<Author> actual = authorsSQLService.getListByFirstNameAndLastName("Napoleon", "Garic");
        List<Author> actual2 = authorsSQLService.getListByFirstNameAndLastName("Napoleon", "Bonaparte");
        List<Author> actual3 = authorsSQLService.getListByFirstNameAndLastName("Romul", "Bonaparte");
        List<Author> actual4 = authorsSQLService.getListByFirstNameAndLastName("Friedrich", "Bonaparte");
        assertAll(
                () -> assertEquals(expected, actual, "Cheking searchinig by firstName"),
                () -> assertEquals(expected, actual2, "Cheking searchinig by firstName and lastName"),
                () -> assertEquals(expected, actual3, "Cheking searchinig by lastName"),
                () -> assertEquals(expectedList, actual4, "Cheking searchinig by firstName of few authors")
        );

    }
}