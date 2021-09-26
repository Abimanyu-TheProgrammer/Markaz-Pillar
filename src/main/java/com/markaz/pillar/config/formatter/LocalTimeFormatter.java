package com.markaz.pillar.config.formatter;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String text, Locale locale) {
        return LocalTime.parse(text, DateTimeFormatter.ISO_TIME);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return DateTimeFormatter.ISO_TIME.format(object);
    }
}

