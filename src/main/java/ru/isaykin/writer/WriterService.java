package ru.isaykin.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.isaykin.reader.Author;
import ru.isaykin.reader.DataBaseRepository;

import java.util.List;


@Component
public class WriterService {

    private final CSVWriter csvWriter;
    private final XLSWriter xlsWriter;
    private final MySQLWriter mySQLWriter;
    private final DataBaseRepository dataBaseRepository;
    private List<Author> authorList;


    @Autowired
    public WriterService(CSVWriter csvWriter, XLSWriter xlsWriter, MySQLWriter mySQLWriter, DataBaseRepository dataBaseRepository) {
        this.csvWriter = csvWriter;
        this.xlsWriter = xlsWriter;
        this.mySQLWriter = mySQLWriter;
        this.dataBaseRepository = dataBaseRepository;

    }

    public void writeAllWithAge(int age){
        authorList = dataBaseRepository.getAuthorsWithAge(age);
       csvWriter.writeToCSV(authorList);
       xlsWriter.writeToXLS(authorList);
       mySQLWriter.exportNewTableToSQLBase(authorList);
    }

}
