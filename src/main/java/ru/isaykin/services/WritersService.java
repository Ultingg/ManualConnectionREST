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
            throw new AuthorNotFoundException("Authors not found. File not created.");
        }
        xlsWriter.writeToXLS(authorList);
    }

    public void writeAllToCSV() {
        List<Author> authorList = new ArrayList<>();
        authorRepo.findAll().iterator().forEachRemaining(authorList::add);
        if (authorList.isEmpty()) {
            throw new AuthorNotFoundException("Authors not found. File not created.");
        }
        csvWriter.writeToCSV(authorList);
    }

    public void writeAllByAgeGTToXLS(int age) {
        List<Author> authorList = authorRepo.getListByAgeGraterThen(ageToDateConverter(age));
        if (authorList.isEmpty()) {
            throw new AuthorNotFoundException("Authors not found. File not created.");
        }
        xlsWriter.writeToXLS(authorList);
    }

    public void writeAllByAgeLTToXLS(int age) {
        List<Author> authorList = authorRepo.getListByAgeLessThen(ageToDateConverter(age));
        if (authorList.isEmpty()) {
            throw new AuthorNotFoundException("Authors not found. File not created.");
        }
        xlsWriter.writeToXLS(authorList);
    }


    public void writeAllByAgeLTToCSV(int age) {
        List<Author> authorList = authorRepo.getListByAgeLessThen(ageToDateConverter(age));
        if (authorList.isEmpty()) {
            throw new AuthorNotFoundException("Authors not found. File not created.");
        }
        csvWriter.writeToCSV(authorList);
    }

    public void writeAllByAgeGTToCSV(int age) {
        List<Author> authorList = authorRepo.getListByAgeGraterThen(ageToDateConverter(age));
        if (authorList.isEmpty()) {
            throw new AuthorNotFoundException("Authors not found. File not created.");
        }
        csvWriter.writeToCSV(authorList);
    }

    private Date ageToDateConverter(int age) {
        return Date.valueOf(LocalDate.now().minusYears(age));
    }
}
