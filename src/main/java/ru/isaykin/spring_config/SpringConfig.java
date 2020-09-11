package ru.isaykin.spring_config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.datasource.driver-class-name}")
    String className;

    @Bean
    DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();

        driverManagerDataSource.setDriverClassName(className);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        driverManagerDataSource.setUrl(url);

        return driverManagerDataSource;
    }
}
