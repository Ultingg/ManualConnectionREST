package ru.isaykin.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.isaykin.reader.Author;
import ru.isaykin.repository.AuthorRepo;
import ru.isaykin.writer.CSVWriter;
import ru.isaykin.writer.XLSWriter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WritersServiceTest {
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
    void writeAllToXLS_valid_success() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.getAll()).thenReturn(authorList);

        boolean actual = writersService.writeAllToXLS();

        assertTrue(actual, "Checking if writeAllToXLS gets True");
        verify(authorRepo, times(1)).getAll();
        verify(xlsWriter, times(1)).writeToXLS(anyList());
        verify(xlsWriter, times(1)).writeToXLS(authorList);
    }

    @Test
    void writeAllToXLS_EmptyList_False() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.getAll()).thenReturn(authorList);

        boolean actual = writersService.writeAllToXLS();

        assertFalse(actual, "Checking if writeAllToXLS gets False");
        verify(authorRepo, times(1)).getAll();
    }

    @Test
    void writeAllToCSV_valid_success() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.getAll()).thenReturn(authorList);

        boolean actual = writersService.writeAllToCSV();

        assertTrue(actual, "Checking if writeAllToCSV gets True");
        verify(authorRepo, times(1)).getAll();
        verify(csvWriter, times(1)).writeToCSV(anyList());
        verify(csvWriter, times(1)).writeToCSV(authorList);
    }

    @Test
    void writeAllToCSV_EmptyList_False() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.getAll()).thenReturn(authorList);

        boolean actual = writersService.writeAllToCSV();

        assertFalse(actual, "Checking if writeAllToCSV gets False");
        verify(authorRepo, times(1)).getAll();
    }

    @Test
    void AgeGreaterThenCSV_valid_success() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authorList);
        boolean actual = writersService.writeAllByAgeGTToCSV(10);

        assertTrue(actual, "Checking if writeAllByAgeGTToCSV gets True");

        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(Date.valueOf(LocalDate.now().minusYears(10)));
        verify(csvWriter, times(1)).writeToCSV(authorList);
    }

    @Test
    void AgeGreaterThenXLS_valid_success() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authorList);

        boolean actual = writersService.writeAllByAgeGTToXLS(10);

        assertTrue(actual, "Checking if writeAllByAgeGTToXLS gets True");
        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(Date.valueOf(LocalDate.now().minusYears(10)));
        verify(xlsWriter, times(1)).writeToXLS(authorList);
    }

    @Test
    void AgeGreaterThenXLS_EmptyList_False() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authorList);

        boolean actual = writersService.writeAllByAgeGTToXLS(10);

        assertFalse(actual, "Checking if write AllByAgeGTToXLS gets False, when get empty List");
        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(Date.valueOf(LocalDate.now().minusYears(10)));
    }

    @Test
    void AgeGreaterThenCSV_EmptyList_False() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.getListByAgeGraterThen(any(Date.class))).thenReturn(authorList);

        boolean actual = writersService.writeAllByAgeGTToCSV(10);

        assertFalse(actual, "Checking if writeAllByAgeGTToCSV gets False, when get empty List");
        verify(authorRepo, times(1)).getListByAgeGraterThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeGraterThen(Date.valueOf(LocalDate.now().minusYears(10)));
    }

    @Test
    void AgeLessThenCSV_valid_success() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authorList);
        boolean actual = writersService.writeAllByAgeLTToCSV(10);

        assertTrue(actual, "Checking if writeAllByAgeLTToCSV gets True");

        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(Date.valueOf(LocalDate.now().minusYears(10)));
        verify(csvWriter, times(1)).writeToCSV(authorList);
    }

    @Test
    void AgeLessThenXLS_valid_success() {
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authorList = Arrays.asList(author1, author2);
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authorList);
        boolean actual = writersService.writeAllByAgeLTToXLS(10);

        assertTrue(actual, "Checking if writeAllByAgeGTToXLS gets True ");

        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(Date.valueOf(LocalDate.now().minusYears(10)));
        verify(xlsWriter, times(1)).writeToXLS(authorList);
    }

    @Test
    void AgeLessThenCSV_EmptyList_False() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authorList);

        boolean actual = writersService.writeAllByAgeLTToCSV(10);

        assertFalse(actual, "Checking if writeAllByAgeGTToCSV gets False, when get empty List");
        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(Date.valueOf(LocalDate.now().minusYears(10)));
    }

    @Test
    void AgeLessThenXLS_EmptyList_False() {
        List<Author> authorList = new ArrayList<>();
        when(authorRepo.getListByAgeLessThen(any(Date.class))).thenReturn(authorList);

        boolean actual = writersService.writeAllByAgeLTToXLS(10);

        assertFalse(actual, "Checking if writeAllByAgeGTToXLS gets False, when get empty List");
        verify(authorRepo, times(1)).getListByAgeLessThen(any(Date.class));
        verify(authorRepo, times(1)).getListByAgeLessThen(Date.valueOf(LocalDate.now().minusYears(10)));
    }
}