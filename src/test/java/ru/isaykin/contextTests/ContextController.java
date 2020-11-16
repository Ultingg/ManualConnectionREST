package ru.isaykin.contextTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
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
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;

import static java.sql.Date.valueOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ContextController {

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
        Assertions.assertThat(authorController).isNotNull();
        Assertions.assertThat(authorsSQLService).isNotNull();
        Assertions.assertThat(authorRepo).isNotNull();
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
        verify(authorRepo, times(1)).insert("Valera","Tovarish","commrade@profkom.ussr", valueOf(LocalDate.parse("1933-10-17")));
        verify(authorRepo, times(1)).insert(anyString(),anyString(),anyString(), any(Date.class));
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
    void insert_inValidAuthor_400BadRequestAndDuplicationEmailSQL() throws Exception {
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
                    mvcResult.getResolvedException().getClass().equals(SQLIntegrityConstraintViolationException.class);
                })
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof SQLIntegrityConstraintViolationException));
    }


}
