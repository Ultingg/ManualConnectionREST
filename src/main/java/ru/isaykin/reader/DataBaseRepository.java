package ru.isaykin.reader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static ru.isaykin.reader.PropetiesRepo.*;
import static ru.isaykin.reader.PropetiesRepo.getPassword;

public class DataBaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger("ru.isaykin.DataBaseRepository");

    public static Set<Author> getAuthorsWithAge(int age) {
        Connection connection = null;

        Statement statement = null;
        Set<Author> authors = null;

        try {
            connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getSqlRequestWithFilterByAge(age));
            LOGGER.debug("Connection success!");
            authors = convertResultSetToAuthors(resultSet); //помещаем в колле цию
            LOGGER.debug("Collection loaded to ResultSet!");
            connection.close();
            statement.close();
            resultSet.close();

        } catch (SQLException e) {
            LOGGER.debug("Connection faild" + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    LOGGER.debug("Connection closed");
                }
            } catch (SQLException e1) {
                LOGGER.debug("Connection faild!" + e1.getMessage());
            }
            try {
                if (connection != null) {
                    connection.close();
                    LOGGER.debug("Connection closed");
                }
            } catch (SQLException e2) {
                LOGGER.debug("Connection faild2" + e2.getMessage());
            }
        }
        return authors;
    }
    public static Set<Author> getAllAuthors() {
        Set<Author> authors = null;

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            try (Statement statement = connection.createStatement()) {
                LOGGER.debug("connection sucsess");
                ResultSet resultSet = statement.executeQuery(getAllTableRequest());
                authors = convertResultSetToAuthors(resultSet); //помещаем в колле цию
                LOGGER.debug("Collection loaded to ResultSet!");

            }

        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
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
        return "SELECT * FROM authors WHERE birthdate >= '" + currentDateMinusYears + "'";
    }
    private static String getAllTableRequest() {
        return "SELECT * FROM authors";
    }

}
