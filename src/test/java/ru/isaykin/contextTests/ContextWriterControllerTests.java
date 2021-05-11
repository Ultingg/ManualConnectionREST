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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.isaykin.controllers.WriterController;
import ru.isaykin.exceptions.AuthorExceptionHandler;
import ru.isaykin.model.Author;
import ru.isaykin.repository.AuthorRepo;
import ru.isaykin.services.WritersService;
import ru.isaykin.writer.CSVWriter;
import ru.isaykin.writer.XLSWriter;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ContextWriterControllerTests {

    @MockBean
    private AuthorRepo authorRepo;

    @MockBean
    private CSVWriter csvWriter;

    @MockBean
    private XLSWriter xlsWriter;

    @Autowired
    AuthorExceptionHandler authorExceptionHandler;

    @Autowired
    private WriterController writerController;

    @Autowired
    private WritersService writersService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;


    @Test
    void notNullTest() {
        assertThat(csvWriter).isNotNull();
        assertThat(xlsWriter).isNotNull();
        assertThat(writerController).isNotNull();
        assertThat(writersService).isNotNull();
        assertThat(objectMapper).isNotNull();
        assertThat(mvc).isNotNull();
        assertThat(authorRepo).isNotNull();
    }


    @Test
    void writeAllToXLS_valid_200OK() throws Exception {
        List<Author> authors = Arrays.asList(new Author(), new Author());
        when(authorRepo.findAll()).thenReturn(authors);

        mvc.perform(get("/writers/xlsx"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void writeAllToXLS_emptyList_400NOT_FOUND() throws Exception {
        List<Author> authors = Arrays.asList();
        when(authorRepo.findAll()).thenReturn(authors);

        mvc.perform(get("/writers/xlsx"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void writeAllToCSV_valid_200OK() throws Exception {
        List<Author> authors = Arrays.asList(new Author(), new Author());
        when(authorRepo.findAll()).thenReturn(authors);

        mvc.perform(get("/writers/csv"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void writeAllToCSV_emptyList_400NOT_FOUND() throws Exception {
        List<Author> authors = Arrays.asList();
        when(authorRepo.findAll()).thenReturn(authors);

        mvc.perform(get("/writers/csv"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void writeAllByGraterAgeToXLS_valid_200OK() throws Exception {
        List<Author> authors = Arrays.asList(new Author(), new Author());
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authors);

        mvc.perform(get("/writers/xlsx/age/gt/{age}", 30))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void writeAllByGraterAgeToXLS_emptyList_400NOT_FOUND() throws Exception {
        List<Author> authors = Arrays.asList();
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authors);

        mvc.perform(get("/writers/xlsx/age/gt/{age}", 30))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void writeAllByLessAgeToXLS_valid_200OK() throws Exception {
        List<Author> authors = Arrays.asList(new Author(), new Author());
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authors);

        mvc.perform(get("/writers/xlsx/age/lt/{age}", 30))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void writeAllByLessAgeToXLS_emptyList_400NOT_FOUND() throws Exception {
        List<Author> authors = Arrays.asList();
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authors);

        mvc.perform(get("/writers/xlsx/age/lt/{age}", 30))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void writeAllByLessAgeToCSV_valid_200OK() throws Exception {
        List<Author> authors = Arrays.asList(new Author(), new Author());
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authors);

        mvc.perform(get("/writers/csv/age/lt/{age}", 30))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void writeAllByLessAgeToCSV_emptyList_400NOT_FOUND() throws Exception {
        List<Author> authors = Arrays.asList();
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authors);

        mvc.perform(get("/writers/csv/age/lt/{age}", 30))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void writeAllByGraterAgeToCSV_valid_200OK() throws Exception {
        List<Author> authors = Arrays.asList(new Author(), new Author());
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authors);

        mvc.perform(get("/writers/csv/age/gt/{age}", 30))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void writeAllByGraterAgeToCSV_emptyList_400NOT_FOUND() throws Exception {
        List<Author> authors = Arrays.asList();
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authors);

        mvc.perform(get("/writers/csv/age/gt/{age}", 30))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
