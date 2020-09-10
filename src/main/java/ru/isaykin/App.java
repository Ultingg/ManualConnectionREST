package ru.isaykin;


import static ru.isaykin.reader.PropertiesRepo.getDataForPropRepo;

public class App {



    public static void main(String[] args) {
        getDataForPropRepo();

//        List<Author> authors = DataBaseRepository.getAuthorsWithAge(30);
//
//        CSVWriter.writeToCSV(authors, getCsvPath());
//        XLSWriter.writeToXLS(authors, getExclePath());
//        MySQLWriter.exportNewTableToSQLBase(authors);
    }
}
