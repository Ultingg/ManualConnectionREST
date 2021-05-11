package ru.isaykin.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.isaykin.services.WritersService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
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
    void writeAllToCSV_valid_Ok() {
        writerController.writeAllToCSV();
        verify(writersService, times(1)).writeAllToCSV();
    }

    @Test
    void writeAllToXLS_valid_Ok() {
        writersService.writeAllToXLS();
        verify(writersService, times(1)).writeAllToXLS();
    }

    @Test
    void writeAllByGraterAgeToXLS_valid_Ok() {
        ResponseEntity<String> expected = new ResponseEntity<>("File created", OK);

        ResponseEntity<String> actual = writerController.writeAllByGraterAgeToXLS(20);

        assertEquals(expected, actual, "Checking if response is correct and XLS file created");
        verify(writersService, times(1)).writeAllByAgeGTToXLS(20);
        verify(writersService, times(1)).writeAllByAgeGTToXLS(anyInt());
    }

    @Test
    void writeAllByGraterAgeToCSV_validAgeRange_Ok() {
        ResponseEntity<String> expected = new ResponseEntity<>("File created", OK);

        ResponseEntity<String> actual = writerController.writeAllByGraterAgeToCSV(20);

        assertEquals(expected, actual, "Checking if response is correct and CSV file created");
        verify(writersService, times(1)).writeAllByAgeGTToCSV(20);
        verify(writersService, times(1)).writeAllByAgeGTToCSV(anyInt());
    }

    @Test
    void writeAllByLessAgeToXLS_validAgeRange_Ok() {
        ResponseEntity<String> expected = new ResponseEntity<>("File created", OK);

        ResponseEntity<String> actual = writerController.writeAllByLessAgeToXLS(20);

        assertEquals(expected, actual, "Checking if response is correct and XLS file created");
        verify(writersService, times(1)).writeAllByAgeLTToXLS(20);
        verify(writersService, times(1)).writeAllByAgeLTToXLS(anyInt());
    }

    @Test
    void writeAllByLessAgeToCSV_validAgeRange_Ok() {
        ResponseEntity<String> expected = new ResponseEntity<>("File created", OK);

        ResponseEntity<String> actual = writerController.writeAllByLessAgeToCSV(20);

        assertEquals(expected, actual, "Checking if response is correct and CSV file created");
        verify(writersService, times(1)).writeAllByAgeLTToCSV(20);
        verify(writersService, times(1)).writeAllByAgeLTToCSV(anyInt());
    }

}
