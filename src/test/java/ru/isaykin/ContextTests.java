package ru.isaykin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import ru.isaykin.controllers.AuthorController;
import ru.isaykin.reader.Author;
import ru.isaykin.repository.AuthorRepo;
import ru.isaykin.services.AuthorsSQLService;

import java.time.LocalDate;

import static java.sql.Date.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
public class ContextTests {

    @Autowired
    private AuthorController authorController;
    @Autowired
    private AuthorRepo authorRepo;
    @Autowired
    private AuthorsSQLService authorsSQLService;

    @Test
    public void contextLoads() throws Exception {
        assertThat(authorController).isNotNull();
        assertThat(authorRepo).isNotNull();
        assertThat(authorsSQLService).isNotNull();
    }

    @Test
    public void ControllerTest() throws Exception {
        Author author = Author.builder()
                .id(1L)
                .firstName("Leonid")
                .lastName("Zub")
                .email("mertz.rosalee@example.net")
                .birthdate(LocalDate.parse("1973-07-13"))
                .build();
        ResponseEntity<?> expected = new ResponseEntity<>(author, OK);

        ResponseEntity<?> actual = authorController.getOneAuthor(1L);

        assertEquals(expected, actual);
    }
    @Test
    public void Insert_DuplicateEmailAuthor_DuplicateException() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Leonid")
                .lastName("Zub")
                .email("mertz.rosalee@example.net")
                .birthdate(LocalDate.parse("1973-07-13"))
                .build();

        assertThrows(DuplicateKeyException.class,()->authorController.insert(author));
    }

    @Test
    void mockService() {
        authorsSQLService = mock(AuthorsSQLService.class);
        authorController = new AuthorController(authorsSQLService);
        Author author = Author.builder()
                .id(101L)
                .firstName("Roman")
                .lastName("Zubarev")
                .email("zubar@example.da")
                .birthdate(LocalDate.parse("1998-01-23"))
                .build();

        when(authorsSQLService.insert(author)).thenReturn(author);
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(author, OK);

        ResponseEntity<?> actualResponse = authorController.insert(author);
        assertEquals(expectedResponse, actualResponse);

        verify(authorsSQLService, times(1)).insert(author);
        verify(authorsSQLService, times(1)).insert(any(Author.class));
    }


    @Test
    public void create() {
        Author author = Author.builder()
                .id(101L)
                .firstName("Roman")
                .lastName("Zubarev")
                .email("zubar@example.da")
                .birthdate(LocalDate.parse("1998-01-23"))
                .build();
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(author, OK);
        ResponseEntity<?> expectedFind = new ResponseEntity<>(author, OK);

        ResponseEntity<?> actualResponse = authorController.insert(author);
        ResponseEntity<?> actualFind = authorController.getOneAuthor(101L);

        assertEquals(expectedResponse, actualResponse);
        assertEquals(expectedFind, actualFind);
    }

    @Test
    void repoTest() {
        Author expectedAuthor = Author.builder()
                .id(101L)
                .firstName("Roman")
                .lastName("Zubarev")
                .email("zubar@example.da")
                .birthdate(LocalDate.parse("1998-01-23"))
                .build();
        authorRepo.insert("Roman", "Zubarev","zubar@example.da", valueOf(LocalDate.parse("1998-01-23")));


        Author actual = authorRepo.getById(101L);

        assertEquals(expectedAuthor,actual);

    }
}
