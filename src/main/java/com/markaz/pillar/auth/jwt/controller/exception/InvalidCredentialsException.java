package com.markaz.pillar.auth.jwt.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCredentialsException extends ResponseStatusException {
    public InvalidCredentialsException(String username) {
        super(HttpStatus.UNAUTHORIZED, String.format("Invalid credentials for %s", username));
    }
    public InvalidCredentialsException(String username, Throwable e) {
        super(HttpStatus.UNAUTHORIZED, String.format("Invalid credentials for %s", username), e);
    }
}
