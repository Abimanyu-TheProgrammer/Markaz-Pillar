package com.markaz.pillar.tools.file.impl;

import com.markaz.pillar.tools.file.FileStorageService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;

@Service("rollingFileStorage")
public class RollingFileStorage extends FileStorageService {
    @Override
    public String getDirectory() {
        Calendar systemDate = Calendar.getInstance();
        return String.format("%s/%d/%d", directory, systemDate.get(Calendar.YEAR), systemDate.get(Calendar.MONTH)+1);
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
