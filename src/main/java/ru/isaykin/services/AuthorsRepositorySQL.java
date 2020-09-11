package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@Slf4j
@Repository
public class AuthorsRepositorySQL {
    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.datasource.driver-class-name}")
    String className;


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
}
