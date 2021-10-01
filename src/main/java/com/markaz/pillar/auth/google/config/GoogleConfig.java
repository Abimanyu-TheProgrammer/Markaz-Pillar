package com.markaz.pillar.auth.google.config;

import com.google.common.util.concurrent.RateLimiter;
import com.markaz.pillar.auth.google.config.handler.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class GoogleConfig {
    @Value("${service.google.limit}")
    private double limit;

    @Value("${service.google.url}")
    private String googleRootUrl;

    @Value("${service.google.auth.url}")
    private String authRootUrl;

    private ErrorHandler errorHandler;

    @Autowired
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Bean
    public RateLimiter limiter() {
        return RateLimiter.create(limit);
    }

    @Bean("googleRestTemplate")
    public RestTemplate googleTemplate() {
        return new RestTemplateBuilder()
                .rootUri(googleRootUrl)
                .setConnectTimeout(Duration.ofSeconds(4))
                .setReadTimeout(Duration.ofSeconds(4))
                .errorHandler(errorHandler)
                .build();
    }

    @Bean("googleAuthRestTemplate")
    public RestTemplate authTemplate() {
        return new RestTemplateBuilder()
                .rootUri(authRootUrl)
                .setConnectTimeout(Duration.ofSeconds(4))
                .setReadTimeout(Duration.ofSeconds(4))
                .build();
    }
}
