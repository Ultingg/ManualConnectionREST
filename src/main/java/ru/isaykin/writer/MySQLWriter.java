package ru.isaykin.writer;

import lombok.extern.slf4j.Slf4j;
import ru.isaykin.reader.Author;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static ru.isaykin.reader.PropetiesRepo.*;

@Slf4j
public class MySQLWriter {
    private static final String DROP_SQL_REQUEST = "DROP TABLE IF EXISTS sortedauthors;";
    private static final String CREATE_SQL_REQUEST = "CREATE TABLE sortedauthors (id INT, FirstName VARCHAR(50), " +
            "LastName VARCHAR(50), Email VARCHAR(100), Birthdate DATE)";

    public static void exportNewTableToSQLBase(List<Author> newTable) {


        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            try (Statement statement = connection.createStatement()) {
                log.debug("connection sucsess");
                statement.executeUpdate(DROP_SQL_REQUEST);
                log.debug("old table droped");
                statement.executeUpdate(CREATE_SQL_REQUEST);
                log.debug("table created");
                for (Author author : newTable) {
                    String sqlRequest = String.format("INSERT sortedauthors (id, FirstName, LastName, Email, Birthdate) " +
                                    "VALUES (%d, '%s', '%s', '%s', '%tF')",
                            author.getId(), author.getFirstName(), author.getLastName().replaceAll("'", "\\\""),
                            author.getEmail().replaceAll("'", "\\\""), author.getBirthDate());
                    statement.executeUpdate(sqlRequest);
                }
                log.debug("table filled");
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
