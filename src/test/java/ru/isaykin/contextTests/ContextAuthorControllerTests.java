package ru.isaykin.contextTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.isaykin.controllers.AuthorController;
import ru.isaykin.exceptions.AuthorExceptionHandler;
import ru.isaykin.model.Author;
import ru.isaykin.repository.AuthorRepo;
import ru.isaykin.services.AuthorsSQLService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ContextAuthorControllerTests {

    @MockBean
    private AuthorRepo authorRepo;

    @Autowired
    AuthorExceptionHandler authorExceptionHandler;

    @Autowired
    private AuthorController authorController;

    @Autowired
    private AuthorsSQLService authorsSQLService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;


    @Test
    void contextLaunch() {
        assertThat(authorController).isNotNull();
        assertThat(authorsSQLService).isNotNull();
        assertThat(authorRepo).isNotNull();
        assertThat(authorExceptionHandler).isNotNull();
    }


    @Test
    void insert_validAuthor_200OKAuthor() throws Exception {
        Author author = Author.builder()
                .firstName("Valera")
                .lastName("Tovarish")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1933-10-17"))
                .build();
        Author authorFromDB = Author.builder()
                .id(1L)
                .firstName("Valera")
                .lastName("Tovarish")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1933-10-17"))
                .build();
        when(authorRepo.save(any(Author.class))).thenReturn(authorFromDB);

        mvc.perform(post("/authors")
                .content(objectMapper.writeValueAsString(author))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorFromDB)));
        verify(authorRepo, times(1)).save(any(Author.class));
    }

    @Test
    void insert_inValidAuthor_400BadRequestAndNotValidElement() throws Exception {
        Author author = Author.builder()
                .id(1L)
                .firstName("Victor")
                .lastName("Tu")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1833-10-17"))
                .build();

        mvc.perform(post("/authors")
                .content(objectMapper.writeValueAsString(author))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
    }


    @Test
    void update_ValidAuthor_200OKUpdatedAuthor() throws Exception {
        Author author = Author.builder()
                .firstName("Petr")
                .lastName("Turner")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1833-10-17"))
                .build();
        Author authorUpdate = Author.builder()
                .id(1L)
                .firstName("Petr")
                .lastName("Turner")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1833-10-17"))
                .build();
        when(authorRepo.existsById(1L)).thenReturn(true);
        when(authorRepo.save(any(Author.class))).thenReturn(authorUpdate);
        mvc.perform(put("/authors/{id}", 1L)
                .content(objectMapper.writeValueAsString(author))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorUpdate)));

        verify(authorRepo, times(1)).existsById(1L);
        verify(authorRepo, times(1)).existsById(anyLong());
        verify(authorRepo, times(1)).save(any(Author.class));
        verify(authorRepo, times(1)).save(authorUpdate);
    }

    @Test
    void update_InValidAuthor_400BadRequest() throws Exception {
        Author authorUpdate = Author.builder()
                .id(1L)
                .firstName("Petr")
                .lastName("Tu")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1833-10-17"))
                .build();

        mvc.perform(put("/authors/{id}", 1L)
                .content(objectMapper.writeValueAsString(authorUpdate))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void update_null_NotFound() throws Exception {
        Author authorUpdate = Author.builder()
                .id(1L)
                .firstName("Petr")
                .lastName("Turner")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1833-10-17"))
                .build();
        when(authorRepo.existsById(1L)).thenReturn(false);

        mvc.perform(put("/authors/{id}", 1L)
                .content(objectMapper.writeValueAsString(authorUpdate))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getListByAgeGraterThen_valid_OK() throws Exception {
        List<Author> authorList = Arrays.asList(new Author(), new Author());
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authorList);

        mvc.perform(get("/authors/age/gt/{age}", 30))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorList)));

        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
    }

    @Test
    void getListByAgeGraterThen_valid_NOT_FOUND() throws Exception {
        List<Author> authorList = Arrays.asList();
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authorList);

        mvc.perform(get("/authors/age/gt/{age}", 30))
                .andExpect(status().isNotFound());

        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
    }

    @Test
    void getListByAgeGraterThen_invalid_BAD_REQUEST() throws Exception {
        List<Author> authorList = Arrays.asList();
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authorList);

        mvc.perform(get("/authors/age/gt/{age}", -30))
                .andExpect(status().isBadRequest());

        verify(authorRepo, times(0)).getListByAgeGraterThen(any(Date.class));
    }

    @Test
    void getListByAgeLessThen_valid_OK() throws Exception {
        List<Author> authorList = Arrays.asList(new Author(), new Author());
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authorList);

        mvc.perform(get("/authors/age/lt/{age}", 30))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorList)));

        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
    }

    @Test
    void getListByAgeLessThen_valid_NOT_FOUND() throws Exception {
        List<Author> authorList = Arrays.asList();
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authorList);

        mvc.perform(get("/authors/age/lt/{age}", 30))
                .andExpect(status().isNotFound());

        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
    }

    @Test
    void getListByAgeLessThen_invalid_BAD_REQUEST() throws Exception {
        List<Author> authorList = Arrays.asList();
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authorList);

        mvc.perform(get("/authors/age/lt/{age}", -30))
                .andExpect(status().isBadRequest());

        verify(authorRepo, times(0)).getListByAgeLessThen(any(Date.class));
    }

    @Test
    void getListOrGetOneByFirstNameAndLastName_valid_ListOK() throws Exception {
        Author author = Author.builder()
                .firstName("Mike")
                .lastName("Lemon")
                .build();
        Author author1 = Author.builder()
                .firstName("Pole")
                .lastName("Lemon")
                .build();

        List<Author> authorList = Arrays.asList(new Author(), new Author());
        when(authorRepo.findAll()).thenReturn(authorList);

        mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorList)));

        verify(authorRepo, times(1)).findAll();
    }

    @Test
    void getListOrGetOneByFirstNameAndLastName_validFirstName_ListOK() throws Exception {
        Author author = Author.builder()
                .firstName("Mike")
                .lastName("Lemon")
                .build();
        Author author1 = Author.builder()
                .firstName("Pole")
                .lastName("Lemon")
                .build();

        List<Author> authorList = Arrays.asList(author);
        when(authorRepo.findAll()).thenReturn(authorList);

        mvc.perform(get("/authors?first_name=Mike"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorList)));

        verify(authorRepo, times(1)).findAll();
    }

    @Test
    void getListOrGetOneByFirstNameAndLastName_validLastName_ListOK() throws Exception {
        Author author = Author.builder()
                .firstName("Mike")
                .lastName("Lemon")
                .build();
        Author author1 = Author.builder()
                .firstName("Pole")
                .lastName("Lemon")
                .build();

        List<Author> authorList = Arrays.asList(author, author1);
        when(authorRepo.findAll()).thenReturn(authorList);

        mvc.perform(get("/authors?last_name=Lemon"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorList)));

        verify(authorRepo, times(1)).findAll();
    }

    @Test
    void getListOrGetOneByFirstNameAndLastName_validLastNameAndFirstName_ListOK() throws Exception {
        Author author = Author.builder()
                .firstName("Mike")
                .lastName("Lemon")
                .build();
        Author author1 = Author.builder()
                .firstName("Jack")
                .lastName("Rose")
                .build();

        List<Author> authorList = Arrays.asList(author, author1);
        when(authorRepo.findAll()).thenReturn(authorList);

        mvc.perform(get("/authors?last_name=Rose&first_name=Mike"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorList)));

        verify(authorRepo, times(1)).findAll();
    }

    @Test
    void getListOrGetOneByFirstNameAndLastName_invalidLastNameAndFirstName_AuthorNotFoundException() throws Exception {
        Author author = Author.builder()
                .firstName("Mike")
                .lastName("Lemon")
                .build();
        Author author1 = Author.builder()
                .firstName("Jack")
                .lastName("Rose")
                .build();

        List<Author> authorList = Arrays.asList(author, author1);
        when(authorRepo.findAll()).thenReturn(authorList);

        mvc.perform(get("/authors?last_name=Tornton&first_name=Gabby"))
                .andExpect(status().isNotFound());

        verify(authorRepo, times(1)).findAll();
    }

    @Test
    void getOneAuthor_validId_AuthorOK() throws Exception {
        Author author = Author.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Lemon")
                .build();
        when(authorRepo.findById(1L)).thenReturn(ofNullable(author));

        mvc.perform(get("/authors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(author)));

        verify(authorRepo, times(1)).findById(1L);
        verify(authorRepo, times(1)).findById(anyLong());
    }

    @Test
    void getOneAuthor_invalidId_400NO_FOUND() throws Exception {
        mvc.perform(get("/authors/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(authorRepo, times(1)).findById(1L);
        verify(authorRepo, times(1)).findById(anyLong());
    }

}
