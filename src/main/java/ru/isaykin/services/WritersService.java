package ru.isaykin.services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.isaykin.exceptions.AuthorNotFoundException;
import ru.isaykin.model.Author;
import ru.isaykin.repository.AuthorRepo;
import ru.isaykin.writer.CSVWriter;
import ru.isaykin.writer.XLSWriter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class WritersService {
    private final AuthorRepo authorRepo;
    private final XLSWriter xlsWriter;
    private final CSVWriter csvWriter;

    public WritersService(AuthorRepo authorRepo, XLSWriter xlsWriter, CSVWriter csvWriter) {
        this.authorRepo = authorRepo;
        this.xlsWriter = xlsWriter;
        this.csvWriter = csvWriter;
    }

    public void writeAllToXLS() {
        List<Author> authorList = new ArrayList<>();
        authorRepo.findAll().iterator().forEachRemaining(authorList::add);
        if (authorList.isEmpty()) {
            throw  new AuthorNotFoundException("Authors not found.");
        }
            xlsWriter.writeToXLS(authorList);
    }

    public void writeAllToCSV() {
        List<Author> authorList = new ArrayList<>();
        authorRepo.findAll().iterator().forEachRemaining(authorList::add);
        if (authorList.isEmpty()) {
            throw  new AuthorNotFoundException("Authors not found.");
        }
            csvWriter.writeToCSV(authorList);
    }

    public boolean writeAllByAgeGTToXLS(int age) {
        List<Author> authorList = authorRepo.getListByAgeGraterThen(ageToDateConverter(age));
        boolean result = true;
        if (!authorList.isEmpty()) {
            xlsWriter.writeToXLS(authorList);
        } else {
            result = false;
        }
        return result;
    }

    public boolean writeAllByAgeLTToXLS(int age) {
        List<Author> authorList = authorRepo.getListByAgeLessThen(ageToDateConverter(age));
        boolean result = true;
        if (!authorList.isEmpty()) {
            xlsWriter.writeToXLS(authorList);
        } else {
            result = false;
        }
        return result;
    }


    public boolean writeAllByAgeLTToCSV(int age) {
        List<Author> authorList = authorRepo.getListByAgeLessThen(ageToDateConverter(age));
        boolean result = true;
        if (!authorList.isEmpty()) {
            csvWriter.writeToCSV(authorList);
        } else {
            result = false;
        }
        return result;
    }

    public boolean writeAllByAgeGTToCSV(int age) {
        List<Author> authorList = authorRepo.getListByAgeGraterThen(ageToDateConverter(age));
        boolean result = true;
        if (!authorList.isEmpty()) {
            csvWriter.writeToCSV(authorList);
        } else {
            result = false;
        }
        return result;
    }

    private Date ageToDateConverter(int age) {
        return Date.valueOf(LocalDate.now().minusYears(age));
    }
}
