package com.markaz.pillar.tools.file;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class FileStorageService {
    public String saveFile(MultipartFile file, Path path) throws IOException {
        validateFile(file);

        String uploadDir = getRootDirectory() + getDirectory();
        String fileName = cleanFileName(file);
        Path resolved = resolveUploadDir(uploadDir, path, fileName);

        write(resolved, file);

        return resolveAbsoluteURL(getDirectory(), fileName);
    }

    protected void validateFile(MultipartFile file) {
        if(file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty!");
        }
    }

    protected Path resolveUploadDir(String directory, Path path, String fileName) throws IOException {
        Path uploadPath = Paths.get(directory, path.toString());
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        return uploadPath.resolve(fileName);
    }

    protected void write(Path path, MultipartFile file) throws IOException {
        Files.write(path, file.getBytes());
    }

    protected String cleanFileName(MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            fileName = String.format("%s_%s.%s",
                    fileName.substring(0, fileName.lastIndexOf(".")),
                    RandomStringUtils.randomAlphanumeric(8),
                    fileName.substring(fileName.lastIndexOf(".") + 1));
            return fileName;
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid Filename!", e);
        }
    }

    protected abstract String getDirectory();
    protected abstract String getRootDirectory();
    protected abstract String resolveAbsoluteURL(String directory, String filename);
}
