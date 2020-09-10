package ru.isaykin;


import ru.isaykin.reader.Author;
import ru.isaykin.reader.DataBaseRepository;
import ru.isaykin.writer.CSVWriter;
import ru.isaykin.writer.MySQLWriter;
import ru.isaykin.writer.XLSWriter;

import java.util.Set;

import static ru.isaykin.reader.PropertiesRepo.*;

public class App {

    public static void main(String[] args) {
        getDataForPropRepo();

        Set<Author> authors = DataBaseRepository.getAuthorsWithAge(30);

        CSVWriter.writeToCSV(authors, getCsvPath());
        XLSWriter.writeToXLS(authors, getExcelPath());
        MySQLWriter.exportNewTableToSQLBase(authors);
    }
}
