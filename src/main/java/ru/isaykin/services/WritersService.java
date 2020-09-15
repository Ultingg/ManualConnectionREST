package ru.isaykin.services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.isaykin.reader.Author;
import ru.isaykin.repository.AuthorRepo;
import ru.isaykin.writer.CSVWriter;
import ru.isaykin.writer.XLSWriter;

import java.sql.Date;
import java.time.LocalDate;
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

    public boolean writeAllToXLS() {
        List<Author> authorList = authorRepo.getAll();
        if (!authorList.isEmpty()) {
            xlsWriter.writeToXLS(authorList);
            return true;
        } else {
            return false;
        }
    }
    public boolean writeAllToCSV() {
        List<Author> authorList = authorRepo.getAll();
        if (!authorList.isEmpty()) {
            csvWriter.writeToCSV(authorList);
            return true;
        } else {
            return false;
        }
    }

    public boolean writeAllByAgeGTToXLS(int age) {
        List<Author> authorList = authorRepo.getListByAgeGraterThen(ageToDateConverter(age));
        if (!authorList.isEmpty()) {
            xlsWriter.writeToXLS(authorList);
            return true;
        } else {
            return false;
        }
    }

    public boolean writeAllByAgeLTToXLS(int age) {
        List<Author> authorList = authorRepo.getListByAgeLessThen(ageToDateConverter(age));
        if (!authorList.isEmpty()) {
            xlsWriter.writeToXLS(authorList);
            return true;
        } else {
            return false;
        }
    }


    public boolean writeAllByAgeLTToCSV(int age) {
        List<Author> authorList = authorRepo.getListByAgeLessThen(ageToDateConverter(age));
        if (!authorList.isEmpty()) {
            csvWriter.writeToCSV(authorList);
            return true;
        } else {
            return false;
        }
    }

    public boolean writeAllByAgeGTToCSV(int age) {
        List<Author> authorList = authorRepo.getListByAgeGraterThen(ageToDateConverter(age));
        if (!authorList.isEmpty()) {
            csvWriter.writeToCSV(authorList);
            return true;
        } else {
            return false;
        }
    }
    private Date ageToDateConverter(int age) {
        return  Date.
                valueOf(LocalDate.now().minusYears(age));
    }
}
