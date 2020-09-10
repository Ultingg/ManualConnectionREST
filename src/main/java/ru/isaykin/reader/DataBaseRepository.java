package ru.isaykin.reader;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.isaykin.reader.PropetiesRepo.*;

@Component
@Slf4j
public class DataBaseRepository {


    public static List<Author> getAuthorsWithAge(int age) {
        Connection connection = null;

        Statement statement = null;
        List<Author> authors = null;

        try {
            connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getSqlRequestWithFilterByAge(age));
            log.debug("Connection success!");
            authors = new ArrayList<>(convertResultSetToAuthors(resultSet)); //помещаем в колле цию
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

    public static List<Author> getAllAuthors() {
        ArrayList<Author> authors = null;

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            try (Statement statement = connection.createStatement()) {
                log.debug("connection sucsess");
                ResultSet resultSet = statement.executeQuery(getAllTableRequest());
                authors = new ArrayList<>(convertResultSetToAuthors(resultSet)); //помещаем в колле цию
                log.debug("Collection loaded to ResultSet!");

            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return authors;
    }

    private static List<Author> convertResultSetToAuthors(ResultSet result) throws SQLException {
        List<Author> authorList = new ArrayList<>();

        while (result.next()) {
            Author author = new Author();
            author.setId(result.getInt("id"));
            author.setFirstName(result.getString("first_name"));
            author.setLastName(result.getString("last_name"));
            author.setEmail(result.getString("email"));
            author.setBirthDate(result.getDate("birthdate").toLocalDate());

            authorList.add(author);
        }
        return authorList;
    }

    private static String getSqlRequestWithFilterByAge(int yearsAsFilter) {
        String currentDateMinusYears = Date.
            valueOf(LocalDate.now().minusYears(yearsAsFilter)).
            toString();
        return "SELECT * FROM authors WHERE birthdate >= '".concat(currentDateMinusYears).concat("'");
    }

    private static String getAllTableRequest() {
        return "SELECT * FROM authors";
    }

}
