package ru.isaykin.reader;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DataBaseRepository {
    private final DataSource dataSource;

    public DataBaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Author> getAuthorsWithAge(int age) {
        Date currentDateMinusYears = Date.
                valueOf(LocalDate.now().minusYears(age));
        String ageRequestSQL = "SELECT * FROM authors WHERE birthdate >= ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Author> authors = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ageRequestSQL);
            preparedStatement.setDate(1, currentDateMinusYears);
            ResultSet resultSet = preparedStatement.executeQuery();
            log.debug("Connection success!");

            authors = convertResultSetToAuthors(resultSet); //помещаем в колле цию
            log.debug("Collection loaded to ResultSet!");

            connection.close();
            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            log.debug("Connection failed" + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    log.debug("Connection closed");
                }
            } catch (SQLException e1) {
                log.debug("Connection failed!" + e1.getMessage());
            }
            try {
                if (connection != null) {
                    connection.close();
                    log.debug("Connection closed");
                }
            } catch (SQLException e2) {
                log.debug("Connection failed2" + e2.getMessage());
            }
        }
        return authors;
    }

    public List<Author> getAllAuthors() {
        List<Author> authors = null;

        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                log.debug("connection success");

                ResultSet resultSet = statement.executeQuery("SELECT * FROM authors");
                authors = convertResultSetToAuthors(resultSet); //помещаем в колле цию
                log.debug("Collection loaded to ResultSet!");

            }

        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
        return authors;
    }

    public static List<Author> convertResultSetToAuthors(ResultSet result) throws SQLException {
        List<Author> authorSet = new ArrayList<>();

        while (result.next()) {
            Author author = new Author();
            author.setId(result.getInt("id"));
            author.setFirstName(result.getString("first_name"));
            author.setLastName(result.getString("last_name"));
            author.setEmail(result.getString("email"));
            author.setBirthDate(result.getDate("birthdate").toLocalDate());

            authorSet.add(author);
        }
        return authorSet;
    }


}
