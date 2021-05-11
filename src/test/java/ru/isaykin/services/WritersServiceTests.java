package ru.isaykin.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.isaykin.exceptions.AuthorNotFoundException;
import ru.isaykin.model.Author;
import ru.isaykin.repository.AuthorRepo;
import ru.isaykin.writer.CSVWriter;
import ru.isaykin.writer.XLSWriter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WritersServiceTests {
    private AuthorRepo authorRepo;
    private WritersService writersService;
    private XLSWriter xlsWriter;
    private CSVWriter csvWriter;

    @BeforeEach
    void setUp() {
        authorRepo = mock(AuthorRepo.class);
        xlsWriter = mock(XLSWriter.class);
        csvWriter = mock(CSVWriter.class);
        writersService = new WritersService(authorRepo, xlsWriter, csvWriter);
    }

    @Test
    void writeAllToXLS_validList_ok() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.findAll()).thenReturn(authorList);

        writersService.writeAllToXLS();

        verify(authorRepo, times(1)).findAll();
        verify(xlsWriter, times(1)).writeToXLS(anyList());
        verify(xlsWriter, times(1)).writeToXLS(authorList);
    }

    @Test
    void writeAllToXLS_emptyList_AuthorNotFoundException() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.findAll()).thenReturn(authorList);

        assertThrows(AuthorNotFoundException.class, () -> writersService.writeAllToXLS());
        verify(authorRepo, times(1)).findAll();
    }

    @Test
    void writeAllToCSV_validList_ok() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.findAll()).thenReturn(authorList);

        writersService.writeAllToCSV();

        verify(authorRepo, times(1)).findAll();
        verify(csvWriter, times(1)).writeToCSV(anyList());
        verify(csvWriter, times(1)).writeToCSV(authorList);
    }

    @Test
    void writeAllToCSV_emptyList_AuthorNotFoundException() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.findAll()).thenReturn(authorList);

        assertThrows(AuthorNotFoundException.class, () -> writersService.writeAllToCSV());
        verify(authorRepo, times(1)).findAll();
    }

    @Test
    void ageGreaterThenCSV_validList_ok() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authorList);
        writersService.writeAllByAgeGTToCSV(10);

        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(Date.valueOf(LocalDate.now().minusYears(10)));
        verify(csvWriter, times(1)).writeToCSV(authorList);
    }

    @Test
    void ageGreaterThenXLS_validList_ok() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authorList);

        writersService.writeAllByAgeGTToXLS(10);

        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(Date.valueOf(LocalDate.now().minusYears(10)));
        verify(xlsWriter, times(1)).writeToXLS(authorList);
    }

    @Test
    void ageGreaterThenXLS_emptyList_AuthorNotFoundException() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authorList);

        assertThrows(AuthorNotFoundException.class, () -> writersService.writeAllByAgeGTToXLS(10));

        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(Date.valueOf(LocalDate.now().minusYears(10)));
    }

    @Test
    void ageGreaterThenCSV_emptyList_AuthorNotFoundException() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authorList);

        assertThrows(AuthorNotFoundException.class, () -> writersService.writeAllByAgeGTToCSV(10));

        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(Date.valueOf(LocalDate.now().minusYears(10)));
    }

    @Test
    void ageLessThenCSV_validList_ok() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authorList);

        writersService.writeAllByAgeLTToCSV(10);

        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(Date.valueOf(LocalDate.now().minusYears(10)));
        verify(csvWriter, times(1)).writeToCSV(authorList);
    }

    @Test
    void ageLessThenXLS_validList_ok() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authorList);

        writersService.writeAllByAgeLTToXLS(10);

        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(Date.valueOf(LocalDate.now().minusYears(10)));
        verify(xlsWriter, times(1)).writeToXLS(authorList);
    }

    @Test
    void ageLessThenCSV_emptyList_AuthorNotFoundException() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authorList);

        assertThrows(AuthorNotFoundException.class, () -> writersService.writeAllByAgeLTToCSV(10));

        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(Date.valueOf(LocalDate.now().minusYears(10)));
    }

    @Test
    void ageLessThenXLS_emptyList_AuthorNotFoundException() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authorList);

        assertThrows(AuthorNotFoundException.class, () -> writersService.writeAllByAgeLTToXLS(10));

        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(Date.valueOf(LocalDate.now().minusYears(10)));
    }
}