package ru.isaykin;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;
import static ru.isaykin.reader.PropertiesRepo.getDataForPropRepo;

@SpringBootApplication
public class RestStarter {

    public static void main(String[] args) {
        getDataForPropRepo();
        run(RestStarter.class, args);
    }
}
