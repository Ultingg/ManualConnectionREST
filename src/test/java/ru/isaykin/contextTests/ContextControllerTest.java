package ru.isaykin.contextTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.isaykin.controllers.AuthorController;
import ru.isaykin.reader.Author;
import ru.isaykin.repository.AuthorRepo;
import ru.isaykin.services.AuthorsSQLService;

import java.sql.Date;
import java.time.LocalDate;

import static java.sql.Date.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ContextControllerTest {

    @MockBean
    private AuthorRepo authorRepo;
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
    }

    @Test
    void insert_validAuthor_200OKAuthor() throws Exception {
        Author author = Author.builder()
                .id(1L)
                .firstName("Valera")
                .lastName("Tovarish")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1933-10-17"))
                .build();
        when(authorRepo.getByEmail("commrade@profkom.ussr")).thenReturn(author);

        mvc.perform(post("/authors")
                .content(objectMapper.writeValueAsString(author))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(author)));
        verify(authorRepo, times(1)).getByEmail(anyString());
        verify(authorRepo, times(1)).getByEmail("commrade@profkom.ussr");
        verify(authorRepo, times(1)).getByEmail(anyString());
        verify(authorRepo, times(1)).insert("Valera", "Tovarish", "commrade@profkom.ussr", valueOf(LocalDate.parse("1933-10-17")));
        verify(authorRepo, times(1)).insert(anyString(), anyString(), anyString(), any(Date.class));
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
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> {
                    mvcResult.getResolvedException().getClass().equals(MethodArgumentNotValidException.class);
                })
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
    }


    @Test
    void update_ValidAuthor_200OKUpdatedAuthor() throws Exception {
        Author author = Author.builder()
                .id(1L)
                .firstName("Victor")
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
        when(authorRepo.getById(1L)).thenReturn(author);

        mvc.perform(put("/authors/{id}", 1L)
                .content(objectMapper.writeValueAsString(authorUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorUpdate)));

        verify(authorRepo, times(1)).getById(1L);
        verify(authorRepo, times(1)).getById(anyLong());
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
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> {
                    mvcResult.getResolvedException().getClass().equals(MethodArgumentNotValidException.class);
                })
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void update_null_NotModified() throws Exception {
        Author authorUpdate = Author.builder()
                .id(1L)
                .firstName("Petr")
                .lastName("Turner")
                .email("commrade@profkom.ussr")
                .birthdate(LocalDate.parse("1833-10-17"))
                .build();
        when(authorRepo.getById(1L)).thenReturn(null);

        mvc.perform(put("/authors/{id}", 1L)
        .content(objectMapper.writeValueAsString(authorUpdate))
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotModified());
    }


}
