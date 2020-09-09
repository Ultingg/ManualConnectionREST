package ru.isaykin.writer;

import ru.isaykin.reader.DataBaseRepository;

public interface HintService {
    void run(DataBaseRepository dataBaseRepository, CSVWriter csvWriter, XLSWriter xlsWriter, MySQLWriter mySQLWriter);
}
