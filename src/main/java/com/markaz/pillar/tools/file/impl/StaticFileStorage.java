package com.markaz.pillar.tools.file.impl;

import com.markaz.pillar.tools.file.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service("staticFileStorage")
public class StaticFileStorage extends FileStorageService {
    @Value("${service.storage.root}")
    private String rootDir;

    @Value("${service.storage.static.dir}")
    private String directory;

    @Value("${service.storage.url}")
    private String rootUrl;

    @Value("#{'${service.storage.allowed-content-types}'.split(',')}")
    private List<String> allowedContentTypes;

    @Override
    public String getDirectory() {
        return directory;
    }

    @Override
    public String getRootDirectory() {
        return rootDir;
    }

    @Override
    public String resolveAbsoluteURL(Path relativeDir, String filename) {
        return String.format("%s%s/%s", rootUrl, relativeDir.toString(), filename);
    }

    @Override
    protected List<String> getAllowedContentType() {
        return allowedContentTypes;
    }
}
