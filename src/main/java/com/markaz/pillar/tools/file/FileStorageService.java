package com.markaz.pillar.tools.file;

import com.github.slugify.Slugify;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Getter(value = AccessLevel.PROTECTED)
public abstract class FileStorageService {
    @Value("${service.storage.root}")
    private String rootDir;

    @Value("${service.storage.static.dir}")
    private String dir;

    @Value("${service.storage.url}")
    private String rootUrl;

    private final Slugify slugify;

    protected FileStorageService() {
        this.slugify = new Slugify();
    }

    public String saveFile(MultipartFile file, Path path) throws IOException {
        validateFile(file);

        Path relativeDir = Paths.get(getDirectory(), path.toString());
        String fileName = cleanFileName(file);
        Path resolved = resolveUploadDir(relativeDir, fileName);

        write(resolved, file);

        return resolveAbsoluteURL(relativeDir, fileName);
    }

    public void validateFile(MultipartFile file) {
        if(file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty!");
        }

        String contentType = file.getContentType();
        if(!getAllowedContentType().contains(contentType)) {
            throw new IllegalArgumentException(String.format("File type %s is not allowed!", contentType));
        }

        DataSize dataSize = DataSize.parse(getAllowedFileSize());
        if(file.getSize() > dataSize.toBytes()) {
            throw new MaxUploadSizeExceededException(dataSize.toBytes());
        }
    }

    protected Path resolveUploadDir(Path relativeDir, String fileName) throws IOException {
        Path path = Paths.get(getRootDirectory(), relativeDir.toString());
        if(!Files.exists(path)) {
            Files.createDirectories(path);
        }

        return path.resolve(fileName);
    }

    protected void write(Path path, MultipartFile file) throws IOException {
        Files.write(path, file.getBytes());
    }

    protected String cleanFileName(MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            fileName = String.format(
                    "%s_%s.%s",
                    slugify.slugify(fileName.substring(0, fileName.lastIndexOf('.'))),
                    RandomStringUtils.randomAlphanumeric(8),
                    fileName.substring(fileName.lastIndexOf('.') + 1)
            );

            return fileName;
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid Filename!", e);
        }
    }

    protected abstract String getDirectory();
    protected abstract String getRootDirectory();
    protected abstract String resolveAbsoluteURL(Path relativeDir, String filename);
    protected abstract List<String> getAllowedContentType();
    protected abstract String getAllowedFileSize();
}
