package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.isaykin.reader.Author;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Repository
public class AuthorsRepositorySQL {


    private final DataSource dataSource;

    public AuthorsRepositorySQL(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    public List<Author> getAll() {
        List<Author> authorList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM authors")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                authorList = convertResultSetToAuthors(resultSet);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return authorList;
    }

    public void requestToTable(String request) {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(request);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public List<Author> getListOfAuthors(String request) throws NullPointerException {
        List<Author> authorsList = null;
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                authorsList = convertResultSetToAuthors(resultSet);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return authorsList;
    }

    public List<Author> getAuthorsWithAge(int age) {
        Date currentDateMinusYears = Date.
                valueOf(LocalDate.now().minusYears(age));

        String ageRequestSQL = "SELECT * FROM authors WHERE birthdate >= ?";

        List<Author> authors = null;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ageRequestSQL)) {
                preparedStatement.setDate(1, currentDateMinusYears);
                ResultSet resultSet = preparedStatement.executeQuery();
                authors = convertResultSetToAuthors(resultSet);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return authors;
    }


    private static List<Author> convertResultSetToAuthors(ResultSet result) throws SQLException {

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
