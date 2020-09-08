package ru.isaykin.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.isaykin.reader.Author;

import java.sql.*;
import java.util.Set;

import static ru.isaykin.reader.PropetiesRepo.*;

@Slf4j
@Component
public class MySQLWriter {
    private static final String DROP_SQL_REQUEST = "DROP TABLE IF EXISTS sortedauthors;";
    private static final String CREATE_SQL_REQUEST = "CREATE TABLE sortedauthors (id INT, FirstName VARCHAR(50), " +
            "LastName VARCHAR(50), Email VARCHAR(100), Birthdate DATE)";


    public void exportNewTableToSQLBase(Set<Author> newTable) {
        String sqlReq = "INSERT sortedauthors (id, FirstName, LastName, Email, Birthdate) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            try (Statement statement = connection.createStatement()) {
                log.debug("connection sucsess");
                statement.executeUpdate(DROP_SQL_REQUEST);
                log.debug("old table droped");
                statement.executeUpdate(CREATE_SQL_REQUEST);
                log.debug("table created");
                for (Author author : newTable) {

                    PreparedStatement preparedStatement = connection.prepareStatement(sqlReq);
                    preparedStatement.setInt(1, author.getId());
                    preparedStatement.setString(2, author.getFirstName());
                    preparedStatement.setString(3, author.getLastName().replaceAll("'", "\\\""));
                    preparedStatement.setString(4, author.getEmail().replaceAll("'", "\\\""));
                    preparedStatement.setDate(5, Date.valueOf(author.getBirthdate()));
                    preparedStatement.executeUpdate();
                    preparedStatement.close();

//                    String sqlRequest = String.format("INSERT sortedauthors (id, FirstName, LastName, Email, Birthdate) " +
//                                    "VALUES (%d, '%s', '%s', '%s', '%tF')",
//                            author.getId(), author.getFirstName(), author.getLastName().replaceAll("'", "\\\""),
//                            author.getEmail().replaceAll("'", "\\\""), author.getBirthdate());
//                  statement.executeUpdate(sqlRequest);


                }
                log.debug("table filled");
            }

        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
    }
}
