package ru.isaykin;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.isaykin.reader.Author;
import ru.isaykin.reader.DataBaseRepository;
import ru.isaykin.writer.CSVWriter;
import ru.isaykin.writer.MySQLWriter;
import ru.isaykin.writer.XLSWriter;

import java.util.List;
import java.util.Set;

import static ru.isaykin.reader.PropetiesRepo.*;

public class App {


    public static void main(String[] args) {
        Logger Logg = LoggerFactory.getLogger("ru.dataexporter");
        getDataForPropRepo();

        List<Author> authors = DataBaseRepository.getAuthorsWithAge(30);

        CSVWriter.writeToCSV(authors, getCsvPath());
        XLSWriter.writeToXLS(authors, getExclePath());
        MySQLWriter.exportNewTableToSQLBase(authors);
    }
}
