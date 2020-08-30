package ru.isaykin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static ru.isaykin.reader.PropetiesRepo.getDataForPropRepo;

@SpringBootApplication
public class RestStrater  {
    public static void main(String[] args) {
        getDataForPropRepo();
        SpringApplication.run(RestStrater.class, args);
    }
}
