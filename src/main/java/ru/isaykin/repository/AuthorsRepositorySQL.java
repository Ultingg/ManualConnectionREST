package ru.isaykin.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.isaykin.reader.Author;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Component
@Repository
public class AuthorsRepositorySQL {


    private final DataSource dataSource;

    public AuthorsRepositorySQL(DataSource dataSource) {
        this.dataSource = dataSource;

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
