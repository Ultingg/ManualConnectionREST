package ru.isaykin;


import org.springframework.stereotype.Component;
import ru.isaykin.reader.DataBaseRepository;
import ru.isaykin.writer.CSVWriter;
import ru.isaykin.writer.MySQLWriter;
import ru.isaykin.writer.WriterService;
import ru.isaykin.writer.XLSWriter;

@Component
public class App {
    DataBaseRepository dataBaseRepository;
    WriterService writerService;
    CSVWriter csvWriter;
    XLSWriter xlsWriter;
    MySQLWriter mySQLWriter;




    public static void main(String[] args) {

//                 Set<Author> authors = new App(new DataBaseRepository(new DataSource())).dataBaseRepository.getAuthorsWithAge(30);
//
//        CSVWriter.writeToCSV(authors, csvPath);
//        XLSWriter.writeToXLS(authors, excelPath);
//        MySQLWriter.exportNewTableToSQLBase(authors);
    }
}
