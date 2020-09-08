package ru.isaykin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.isaykin.reader.DataBaseRepository;
import ru.isaykin.writer.WriterService;

@Component
public class App {
    DataBaseRepository dataBaseRepository;
    WriterService writerService;


    @Autowired
    public App(DataBaseRepository dataBaseRepository) {
        this.dataBaseRepository = dataBaseRepository;
    }

    public static void main(String[] args) {




//                 Set<Author> authors = new App(new DataBaseRepository(new DataSource())).dataBaseRepository.getAuthorsWithAge(30);
//
//        CSVWriter.writeToCSV(authors, csvPath);
//        XLSWriter.writeToXLS(authors, excelPath);
//        MySQLWriter.exportNewTableToSQLBase(authors);
    }
}
