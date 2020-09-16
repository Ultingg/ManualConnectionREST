package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import ru.isaykin.spring_config.FileStorageProperties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            log.error("Could not create the directory where the uploaded files will be stored." + e.getMessage());
        }

    }

    public Resource loadFileAsResource(String fileName) {
        Resource resource = null;
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found" + fileName);
            }
        } catch (MalformedURLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return resource;
    }
}
