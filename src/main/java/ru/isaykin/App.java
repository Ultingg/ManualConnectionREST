package ru.isaykin;


import org.springframework.stereotype.Component;
import ru.isaykin.reader.Author;
import ru.isaykin.reader.DataBaseRepository;
import ru.isaykin.writer.CSVWriter;
import ru.isaykin.writer.MySQLWriter;
import ru.isaykin.writer.XLSWriter;

import java.util.List;

@Component
public class App {




    public static void main(String[] args) {
        List<Author> authors = new DataBaseRepository().getAuthorsWithAge(30);
    XLSWriter xlsWriter = new XLSWriter();
    CSVWriter csvWriter = new CSVWriter();
    MySQLWriter mySQLWriter = new MySQLWriter();
    csvWriter.writeToCSV(authors);
    xlsWriter.writeToXLS(authors);
    mySQLWriter.exportNewTableToSQLBase(authors);
    }
}
