package com.markaz.pillar.tools.file.impl;

import com.markaz.pillar.tools.file.FileStorageService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service("staticFileStorage")
public class StaticFileStorage extends FileStorageService {
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
        return getContentTypes();
    }
}
