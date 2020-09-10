package ru.isaykin.writer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.isaykin.reader.Author;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
public class CSVWriter {
    @Value("${user.csvpath}")
    String csvPath;

    public void writeToCSV(List<Author> authors) {
        BufferedWriter writer;
        try {

            writer = Files.newBufferedWriter(Paths.get(csvPath));

            CSVPrinter printer = CSVFormat.DEFAULT.withHeader("ID", "First name", "Last name", "E-mail", "Birhtdate").print(writer);
            for (Author au : authors) {
                printer.printRecord(au.getId(), au.getFirstName(), au.getLastName(), au.getEmail(), au.getBirthDate());
            }
            log.debug("CSVFile created");
            printer.flush();
            writer.close();
        } catch (IOException e) {
            log.debug("Error in CSVWriter" + e.getMessage());
        }
    }
}
