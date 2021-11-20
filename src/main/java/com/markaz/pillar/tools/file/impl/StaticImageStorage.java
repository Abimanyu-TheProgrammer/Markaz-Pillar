package com.markaz.pillar.tools.file.impl;

import com.markaz.pillar.tools.file.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service("staticImageStorage")
public class StaticImageStorage extends FileStorageService {
    @Value("#{'${service.storage.image.allowed-content-types}'.split(',')}")
    private List<String> contentTypes;

    @Value("${service.storage.image.max-file-size}")
    private String maxFileSize;

    @Override
    public String getDirectory() {
        return getDir();
    }

    @Override
    public String getRootDirectory() {
        return getRootDir();
    }

    @Override
    public String resolveAbsoluteURL(Path relativeDir, String filename) {
        return String.format("%s%s/%s", getRootUrl(), relativeDir.toString(), filename);
    }

    @Override
    protected List<String> getAllowedContentType() {
        return contentTypes;
    }

    @Override
    protected String getAllowedFileSize() {
        return maxFileSize;
    }
}
