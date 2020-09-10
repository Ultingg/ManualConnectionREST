package ru.isaykin.writer;

import lombok.extern.slf4j.Slf4j;
import ru.isaykin.reader.Author;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import static ru.isaykin.reader.PropertiesRepo.*;

@Slf4j
public class MySQLWriter {
    private static final String DROP_SQL_REQUEST = "DROP TABLE IF EXISTS sortedauthors;";
    private static final String CREATE_SQL_REQUEST = "CREATE TABLE sortedauthors (id INT, FirstName VARCHAR(50), " +
        "LastName VARCHAR(50), Email VARCHAR(100), Birthdate DATE)";

    public static void exportNewTableToSQLBase(Set<Author> newTable) {


        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            try (Statement statement = connection.createStatement()) {
                log.debug("Connection success.");

                statement.executeUpdate(DROP_SQL_REQUEST);
                log.debug("Old table dropped.");

                statement.executeUpdate(CREATE_SQL_REQUEST);
                log.debug("Table created.");

                for (Author author : newTable) {
                    //Date tempDate = Date.valueOf(author.getBirthdate());
                    String sqlRequest = String.format("INSERT sortedauthors (id, FirstName, LastName, Email, Birthdate) " +
                            "VALUES (%d, '%s', '%s', '%s', '%tF')",
                        author.getId(), author.getFirstName(), author.getLastName().replaceAll("'", "\\\""),
                        author.getEmail().replaceAll("'", "\\\""), author.getBirthdate());
                    statement.executeUpdate(sqlRequest);
                }
                log.debug("Table filled.");
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
