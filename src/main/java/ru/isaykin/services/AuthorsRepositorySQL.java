package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.isaykin.reader.Author;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static ru.isaykin.reader.DataBaseRepository.convertResultSetToAuthors;

@Component
@Slf4j
@Repository
public class AuthorsRepositorySQL {


    private final DataSource dataSource;

    public AuthorsRepositorySQL(DataSource dataSource) {
        this.dataSource = dataSource;

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
}
