package ru.isaykin.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.isaykin.reader.Author;
import ru.isaykin.repository.AuthorRepo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorsSQLServiceTest {


    private AuthorRepo authorRepo;
    private AuthorsSQLService authorsSQLService;

    @BeforeEach
    void setUp(){
        authorRepo = Mockito.mock(AuthorRepo.class);
        authorsSQLService = new AuthorsSQLService(authorRepo);
    }


    @Test
    void getAll() {

        Author author = new Author();
        List<Author> authorList = Arrays.asList(author);
        Mockito.when(authorRepo.getAll()).thenReturn(authorList);
        List<Author> expected = authorRepo.getAll();
        assertEquals(expected,authorList);
    }

    @Test
    void insertTest() {
        Author author = new Author();
        author.setEmail("bumblebe@transformer.ab");
        author.setFirstName("Yellow");
        author.setLastName("Car");
        author.setBirthdate(LocalDate.parse("2020-10-12"));

        Mockito.when(authorRepo.getByEmail("bumblebe@transformer.ab")).
                thenReturn(author);

     Author expected = authorsSQLService.insert(author);
     assertEquals(expected,author);


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


    }
}