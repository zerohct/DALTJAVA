package com.app.MediQuirk.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class testservice {

    @Value("${file.upload-dir.internal}")
    private String internalUploadDir;

    @Value("${file.upload-dir.external}")
    private String externalUploadDir;

    private final ResourceLoader resourceLoader;

    public testservice(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String storeFile(MultipartFile file, boolean useExternalStorage) throws IOException {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        int lastIndex = originalFileName.lastIndexOf('.');
        if (lastIndex > 0) {
            fileExtension = originalFileName.substring(lastIndex);
        }
        String fileName = UUID.randomUUID().toString() + fileExtension;

        String uploadDir = useExternalStorage ? externalUploadDir : internalUploadDir;

        Path uploadPath;
        if (useExternalStorage) {
            uploadPath = Paths.get(uploadDir);
        } else {
            Resource resource = resourceLoader.getResource("classpath:" + uploadDir);
            if (resource.exists()) {
                uploadPath = Paths.get(resource.getURI());
            } else {
                uploadPath = Paths.get(uploadDir);
            }
        }

        Files.createDirectories(uploadPath);
        Path targetLocation = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}