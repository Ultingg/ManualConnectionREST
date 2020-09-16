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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Slf4j
@RestController
public class FileDownloadController {

    private final FileStorageService fileStorageService;


    public FileDownloadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }


    @GetMapping("download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String fileName, HttpServletRequest request) {

        Resource resource = fileStorageService.loadFileAsResource(fileName);

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
