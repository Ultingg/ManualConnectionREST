package ru.isaykin.reader;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static ru.isaykin.reader.PropetiesRepo.*;
@Slf4j
public class updateRequest {


    public static void UpdateTable(String request) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(request);
            }
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
    }
}
