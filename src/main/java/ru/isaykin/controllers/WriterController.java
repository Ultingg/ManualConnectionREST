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

    @GetMapping("writers/xls")
    public ResponseEntity<String> writeAllToXlS() {
        ResponseEntity<String> result;
        if (writersService.writeAllToXLS()) {
            return result = new ResponseEntity<>("File created", OK);
        } else {
            return result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
    }

    @GetMapping("writers/xls/age/gt/{age}")
    public ResponseEntity<String> writeAllByGraterAgeToXls(@PathVariable("age") int age) {
        ResponseEntity<String> result;
        if (writersService.writeAllByAgeGTToXLS(age)) {
            return result = new ResponseEntity<>("File created", OK);
        } else {
            return result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
    }

    @GetMapping("writers/xls/age/lt/{age}")
    public ResponseEntity<String> writeAllByLessAgeToXls(@PathVariable("age") int age) {
        ResponseEntity<String> result;
        if (writersService.writeAllByAgeLTToXLS(age)) {
            return result = new ResponseEntity<>("File created", OK);
        } else {
            return result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
    }

    @GetMapping("writers/csv")
    public ResponseEntity<String> writeAllToCSV() {
        ResponseEntity<String> result;
        if (writersService.writeAllToCSV()) {
            return result = new ResponseEntity<>("File created", OK);
        } else {
            return result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
    }

    @GetMapping("writers/csv/age/lt/{age}")
    public ResponseEntity<String> writeAllByLessAgeToCSV(@PathVariable("age") int age) {
        ResponseEntity<String> result;
        if (writersService.writeAllByAgeLTToCSV(age)) {
            return result = new ResponseEntity<>("File created", OK);
        } else {
            return result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
    }

    @GetMapping("writers/csv/age/gt/{age}")
    public ResponseEntity<String> writeAllByGraterAgeToCSV(@PathVariable("age") int age) {
        ResponseEntity<String> result;
        if (writersService.writeAllByAgeGTToCSV(age)) {
            return result = new ResponseEntity<>("File created", OK);
        } else {
            return result = new ResponseEntity<>("NO DATA FOUND", NOT_FOUND);
        }
    }

}
