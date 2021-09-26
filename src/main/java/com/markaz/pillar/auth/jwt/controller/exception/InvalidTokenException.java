package com.markaz.pillar.auth.jwt.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidTokenException extends ResponseStatusException {
    public InvalidTokenException(String token) {
        super(HttpStatus.BAD_REQUEST, String.format("Invalid token %s", token));
    }

    public InvalidTokenException(String token, Throwable e) {
        super(HttpStatus.BAD_REQUEST, String.format("Invalid token %s", token), e);
    }
}
