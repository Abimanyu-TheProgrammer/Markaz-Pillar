package com.markaz.pillar.config;

import com.markaz.pillar.config.formatter.LocalDateFormatter;
import com.markaz.pillar.config.formatter.LocalTimeFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FormatterConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateFormatter());
        registry.addFormatter(new LocalTimeFormatter());
    }
}
