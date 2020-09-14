package ru.isaykin.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.isaykin.reader.Author;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class MySQLWriter {
    private final DataSource dataSource;
    private static final String DROP_SQL_REQUEST = "DROP TABLE IF EXISTS sortedauthors;";
    private static final String CREATE_SQL_REQUEST = "CREATE TABLE sortedauthors (id INT, FirstName VARCHAR(50), " +
            "LastName VARCHAR(50), Email VARCHAR(100), Birthdate DATE)";

    public MySQLWriter(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void exportNewTableToSQLBase(List<Author> newTable) {
        String sqlReq = "INSERT sortedauthors (id, FirstName, LastName, Email, Birthdate) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DROP_SQL_REQUEST)) {
                log.debug("Connection success.");

                preparedStatement.executeUpdate();
                log.debug("Old table dropped.");

                preparedStatement.executeUpdate(CREATE_SQL_REQUEST);
                log.debug("Table created.");

                for (Author author : newTable) {

                    PreparedStatement preparedStatement1 = connection.prepareStatement(sqlReq);
                    preparedStatement1.setLong(1, author.getId());
                    preparedStatement1.setString(2, author.getFirstName());
                    preparedStatement1.setString(3, author.getLastName().replaceAll("'", "\\\\\'"));
                    preparedStatement1.setString(4, author.getEmail().replaceAll("'", "\\\\\'"));
                    preparedStatement1.setDate(5, Date.valueOf(author.getBirthdate()));
                    preparedStatement1.executeUpdate();
                    preparedStatement1.close();


                }
                log.debug("Table filled.");
            }

        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
    }
}
