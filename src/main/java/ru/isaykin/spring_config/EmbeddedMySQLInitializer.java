package ru.isaykin.spring_config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

public class EmbeddedMySQLInitializer implements ApplicationContextInitializer<GenericApplicationContext> {
    @Override
    public void initialize(GenericApplicationContext genericApplicationContext) {
//        EmbeddedDatabase mysql = new EmbeddedDatabase() {
//        }
    }
}
