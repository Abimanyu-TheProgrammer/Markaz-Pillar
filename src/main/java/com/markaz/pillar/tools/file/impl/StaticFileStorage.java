package com.markaz.pillar.tools.file.impl;

import com.markaz.pillar.tools.file.FileStorageService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service("staticFileStorage")
public class StaticFileStorage extends FileStorageService {
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
