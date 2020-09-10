package ru.isaykin.reader;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static ru.isaykin.reader.PropertiesRepo.*;

@Component
@Slf4j
public class DataBaseRepository {

    public static final String AUTHORS_WITH_BIRTHSDAY_QUERY = "SELECT * FROM authors WHERE birthdate >= '";

    public static Set<Author> getAuthorsWithAge(int age) {
        Connection connection = null;

        Statement statement = null;
        Set<Author> authors = null;

        try {
            connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getSqlRequestWithFilterByAge(age));
            log.debug("Connection success!");

            authors = convertResultSetToAuthors(resultSet); //помещаем в колле цию
            log.debug("Collection loaded to ResultSet!");

            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            log.error("Connection faild" + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    log.debug("Connection closed");
                }
            } catch (SQLException e1) {
                log.error("Connection faild!" + e1.getMessage());
            }
            try {
                if (connection != null) {
                    connection.close();
                    log.debug("Connection closed");
                }
            } catch (SQLException e2) {
                log.error("Connection faild2" + e2.getMessage());
            }
        }
        return authors;
    }

    public static Set<Author> getAllAuthors() {
        Set<Author> authors = null;

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            try (Statement statement = connection.createStatement()) {
                log.debug("connection success");
                ResultSet resultSet = statement.executeQuery("SELECT * FROM authors");
                authors = convertResultSetToAuthors(resultSet); //помещаем в колле цию
                log.debug("Collection loaded to ResultSet!");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return authors;
    }

    private static Set<Author> convertResultSetToAuthors(ResultSet result) throws SQLException {
        Set<Author> authorSet = new TreeSet<>();

        while (result.next()) {
            Author author = new Author();
            author.setId(result.getInt("id"));
            author.setFirstName(result.getString("first_name"));
            author.setLastName(result.getString("last_name"));
            author.setEmail(result.getString("email"));
            author.setBirthdate(result.getDate("birthdate").toLocalDate());

            authorSet.add(author);
        }
        return authorSet;
    }

    private static String getSqlRequestWithFilterByAge(int yearsAsFilter) {
        String currentDateMinusYears = Date.
            valueOf(LocalDate.now().minusYears(yearsAsFilter)).
            toString();
        return AUTHORS_WITH_BIRTHSDAY_QUERY.concat(currentDateMinusYears).concat("'");
    }
}
