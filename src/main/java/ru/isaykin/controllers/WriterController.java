package ru.isaykin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isaykin.services.WritersService;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping
public class WriterController {
    private final WritersService writersService;

    public WriterController(WritersService writersService) {
        this.writersService = writersService;
    }

    @GetMapping("writers/xlsx")
    public ResponseEntity<String> writeAllToXlS() {
        ResponseEntity<String> result;
        if (writersService.writeAllToXLS()) {
            result = new ResponseEntity<>("File created", OK);
        } else {
            result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
        return result;
    }

    @GetMapping("writers/xlsx/age/gt/{age}")
    public ResponseEntity<String> writeAllByGraterAgeToXls(@PathVariable("age") int age) {
        ResponseEntity<String> result;
        if (writersService.writeAllByAgeGTToXLS(age)) {
            result = new ResponseEntity<>("File created", OK);
        } else {
            result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
        return result;
    }

    @GetMapping("writers/xlsx/age/lt/{age}")
    public ResponseEntity<String> writeAllByLessAgeToXls(@PathVariable("age") int age) {
        ResponseEntity<String> result;
        if (writersService.writeAllByAgeLTToXLS(age)) {
            result = new ResponseEntity<>("File created", OK);
        } else {
            result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
        return result;
    }

    @GetMapping("writers/csv")
    public ResponseEntity<String> writeAllToCSV() {
        ResponseEntity<String> result;
        if (writersService.writeAllToCSV()) {
            result = new ResponseEntity<>("File created", OK);
        } else {
            result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
        return result;
    }

    @GetMapping("writers/csv/age/lt/{age}")
    public ResponseEntity<String> writeAllByLessAgeToCSV(@PathVariable("age") int age) {
        ResponseEntity<String> result;
        if (writersService.writeAllByAgeLTToCSV(age)) {
            result = new ResponseEntity<>("File created", OK);
        } else {
            result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
        return result;
    }

    @GetMapping("writers/csv/age/gt/{age}")
    public ResponseEntity<String> writeAllByGraterAgeToCSV(@PathVariable("age") int age) {
        ResponseEntity<String> result;
        if (writersService.writeAllByAgeGTToCSV(age)) {
            result = new ResponseEntity<>("File created", OK);
        } else {
            result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
        return result;
    }

}
