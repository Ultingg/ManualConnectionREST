package ru.isaykin.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.isaykin.reader.Author;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
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

    private static List<Author> convertResultSetToAuthors(ResultSet result) throws SQLException {

        List<Author> authorList = new ArrayList<>();

        while (result.next()) {
            Author author = new Author();
            author.setId(result.getLong("id"));
            author.setFirstName(result.getString("first_name"));
            author.setLastName(result.getString("last_name"));
            author.setEmail(result.getString("email"));
            author.setBirthdate(result.getDate("birthdate").toLocalDate());

            authorList.add(author);
        }
        return authorList;
    }


    public String createList(List<Author> authorList) {
        int count = 0;
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO authors VALUES ( ?, ?, ?, ?, ?, ?)")) {

                for (Author author : authorList) {
                    preparedStatement.setInt(1, 0);
                    preparedStatement.setString(2, author.getFirstName());
                    preparedStatement.setString(3, author.getLastName());
                    preparedStatement.setString(4, author.getEmail());
                    preparedStatement.setString(5, author.getBirthdate().toString());
                    preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                    preparedStatement.addBatch();
                }
                int[] updates = preparedStatement.executeBatch();
                count = updates.length;
                connection.commit();
                connection.setAutoCommit(true);

            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return "was added: " + count;

    }
}
