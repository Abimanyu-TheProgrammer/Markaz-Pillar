package com.markaz.pillar.auth.google.config.handler;

import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus status = response.getStatusCode();
        if(status.is4xxClientError() || status.is3xxRedirection()) {
            String body = new BufferedReader(
                    new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n")
            );

            String header = new GsonBuilder().setPrettyPrinting().create().toJson(response.getHeaders());

            log.error(String.format(
                    "Google Service %d error%nHeaders:%n%s%nBody:%n%s",
                    response.getRawStatusCode(), header, body
            ));
            throw new ResponseStatusException(status, "Service error, contact admin for more info.");
        } else {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Google is unavailable. Try again later.");
        }
    }
}
