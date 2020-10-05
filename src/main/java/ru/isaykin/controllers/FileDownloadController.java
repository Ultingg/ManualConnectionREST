package ru.isaykin.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.isaykin.services.FileStorageService;
import ru.isaykin.services.WritersService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Slf4j
@RestController
public class FileDownloadController {

    private final FileStorageService fileStorageService;
    private final WritersService writersService;

    public FileDownloadController(FileStorageService fileStorageService, WritersService writersService) {
        this.fileStorageService = fileStorageService;
        this.writersService = writersService;
    }


    @GetMapping("download/xlsx/age/lt/{age}")
    public ResponseEntity<Resource> downloadFileXLSByAgeLessThen(@PathVariable("age") int age, HttpServletRequest request) {
        writersService.writeAllByAgeLTToXLS(age);
        return getResourceResponseXLSEntity(request);
    }

    @GetMapping("download/xlsx/age/gt/{age}")
    public ResponseEntity<Resource> downloadFileXLSByAgeGraterThen(@PathVariable("age") int age, HttpServletRequest request) {
        writersService.writeAllByAgeGTToXLS(age);
        return getResourceResponseXLSEntity(request);
    }

    @GetMapping("download/csv/age/gt/{age}")
    public ResponseEntity<Resource> downloadFileCsvByAgeGraterThen(@PathVariable("age") int age, HttpServletRequest request) {
        writersService.writeAllByAgeGTToCSV(age);
        return getResourceResponseCSVEntity(request);
    }

    @GetMapping("download/csv/age/lt/{age}")
    public ResponseEntity<Resource> downloadFileCsvByAgeLessThen(@PathVariable("age") int age, HttpServletRequest request) {
        writersService.writeAllByAgeLTToCSV(age);
        return getResourceResponseCSVEntity(request);
    }

    @GetMapping("download/csv")
    public ResponseEntity<Resource> downloadFileCsvAllAuthors(HttpServletRequest request) {
        writersService.writeAllToCSV();
        return getResourceResponseCSVEntity(request);
    }

    @GetMapping("download/xlsx")
    public ResponseEntity<Resource> downloadFileXLSAllAuthors(HttpServletRequest request) {
        writersService.writeAllToXLS();
        return getResourceResponseXLSEntity(request);
    }


    private ResponseEntity<Resource> getResourceResponseXLSEntity(HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource("authors.xlsx");

        return getCheckedResourceResponseEntity(request, resource);
    }


    private ResponseEntity<Resource> getResourceResponseCSVEntity(HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource("authors.csv");

        return getCheckedResourceResponseEntity(request, resource);
    }

    private ResponseEntity<Resource> getCheckedResourceResponseEntity(HttpServletRequest request, Resource resource) {
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            log.info("Couldn't determine file type");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
