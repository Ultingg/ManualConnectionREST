package ru.isaykin.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.isaykin.services.WritersService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class WriterControllerTests {
    private WritersService writersService;
    private WriterController writerController;

    @BeforeEach
    public void setUp() {
        writersService = mock(WritersService.class);
        writerController = new WriterController(writersService);
    }

    @Test
    void writeAllToCSV_valid_success() {
        ResponseEntity<String> expected = new ResponseEntity<>("File created", OK);
        when(writersService.writeAllToCSV()).thenReturn(true);

        ResponseEntity<String> actual = writerController.writeAllToCSV();

        assertEquals(expected, actual, "Checking if response is correct and CSV file created");
        verify(writersService, times(1)).writeAllToCSV();
    }

    @Test
    void writeAllToCSV_notValid_success() {
        ResponseEntity<String> expected = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        when(writersService.writeAllToCSV()).thenReturn(false);

        ResponseEntity<String> actual = writerController.writeAllToCSV();

        assertEquals(expected, actual, "Checking if response is not correct and CSV file wasn't created");
        verify(writersService, times(1)).writeAllToCSV();
    }

    @Test
    void writeAllToXLS_valid_success() {
        ResponseEntity<String> expected = new ResponseEntity<>("File created", OK);
        when(writersService.writeAllToXLS()).thenReturn(true);

        ResponseEntity<String> actual = writerController.writeAllToXLS();

        assertEquals(expected, actual, "Checking if response is correct and XLS file created");
        verify(writersService, times(1)).writeAllToXLS();
    }

    @Test
    void writeAllToXLS_notValid_success() {
        ResponseEntity<String> expected = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        when(writersService.writeAllToXLS()).thenReturn(false);

        ResponseEntity<String> actual = writerController.writeAllToXLS();

        assertEquals(expected, actual, "Checking if response is not correct and XLS file wasn't created");
        verify(writersService, times(1)).writeAllToXLS();
    }

    @Test
    void writeAllByGraterAgeToXLS_valid_success() {
        ResponseEntity<String> expected = new ResponseEntity<>("File created", OK);
        when(writersService.writeAllByAgeGTToXLS(20)).thenReturn(true);

        ResponseEntity<String> actual = writerController.writeAllByGraterAgeToXLS(20);

        assertEquals(expected, actual, "Checking if response is correct and XLS file created");
        verify(writersService, times(1)).writeAllByAgeGTToXLS(20);
        verify(writersService, times(1)).writeAllByAgeGTToXLS(anyInt());
    }

    @Test
    void writeAllByGraterAgeToXLS_noExistedAgeRange_notFound() {
        ResponseEntity<String> expected = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        when(writersService.writeAllByAgeGTToXLS(20)).thenReturn(false);

        ResponseEntity<String> actual = writerController.writeAllByGraterAgeToXLS(20);

        assertEquals(expected, actual, "Checking if response is correct and XLS file created");
        verify(writersService, times(1)).writeAllByAgeGTToXLS(20);
        verify(writersService, times(1)).writeAllByAgeGTToXLS(anyInt());
    }

    @Test
    void writeAllByGraterAgeToCSV_valid_success() {
        ResponseEntity<String> expected = new ResponseEntity<>("File created", OK);
        when(writersService.writeAllByAgeGTToCSV(20)).thenReturn(true);

        ResponseEntity<String> actual = writerController.writeAllByGraterAgeToCSV(20);

        assertEquals(expected, actual, "Checking if response is correct and CSV file created");
        verify(writersService, times(1)).writeAllByAgeGTToCSV(20);
        verify(writersService, times(1)).writeAllByAgeGTToCSV(anyInt());
    }

    @Test
    void writeAllByGraterAgeToCSV_noExistedAgeRange_notFound() {
        ResponseEntity<String> expected = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        when(writersService.writeAllByAgeGTToCSV(20)).thenReturn(false);

        ResponseEntity<String> actual = writerController.writeAllByGraterAgeToCSV(20);

        assertEquals(expected, actual, "Checking if response is correct and CSV file created");
        verify(writersService, times(1)).writeAllByAgeGTToCSV(20);
        verify(writersService, times(1)).writeAllByAgeGTToCSV(anyInt());
    }

    @Test
    void writeAllByLessAgeToXLS_valid_success() {
        ResponseEntity<String> expected = new ResponseEntity<>("File created", OK);
        when(writersService.writeAllByAgeLTToXLS(20)).thenReturn(true);

        ResponseEntity<String> actual = writerController.writeAllByLessAgeToXLS(20);

        assertEquals(expected, actual, "Checking if response is correct and XLS file created");
        verify(writersService, times(1)).writeAllByAgeLTToXLS(20);
        verify(writersService, times(1)).writeAllByAgeLTToXLS(anyInt());
    }

    @Test
    void writeAllByLessAgeToXLS_noExistedAgeRange_notFound() {
        ResponseEntity<String> expected = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        when(writersService.writeAllByAgeLTToXLS(20)).thenReturn(false);

        ResponseEntity<String> actual = writerController.writeAllByLessAgeToXLS(20);

        assertEquals(expected, actual, "Checking if response is correct and XLS file created");
        verify(writersService, times(1)).writeAllByAgeLTToXLS(20);
        verify(writersService, times(1)).writeAllByAgeLTToXLS(anyInt());
    }

    @Test
    void writeAllByLessAgeToCSV_valid_success() {
        ResponseEntity<String> expected = new ResponseEntity<>("File created", OK);
        when(writersService.writeAllByAgeLTToCSV(20)).thenReturn(true);

        ResponseEntity<String> actual = writerController.writeAllByLessAgeToCSV(20);

        assertEquals(expected, actual, "Checking if response is correct and CSV file created");
        verify(writersService, times(1)).writeAllByAgeLTToCSV(20);
        verify(writersService, times(1)).writeAllByAgeLTToCSV(anyInt());
    }

    @Test
    void writeAllByLessAgeToCSV_noExistedAgeRange_notFound() {
        ResponseEntity<String> expected = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        when(writersService.writeAllByAgeLTToCSV(20)).thenReturn(false);

        ResponseEntity<String> actual = writerController.writeAllByLessAgeToCSV(20);

        assertEquals(expected, actual, "Checking if response is correct and CSV file created");
        verify(writersService, times(1)).writeAllByAgeLTToCSV(20);
        verify(writersService, times(1)).writeAllByAgeLTToCSV(anyInt());
    }

}
