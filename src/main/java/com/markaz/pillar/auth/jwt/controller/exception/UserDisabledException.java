package com.markaz.pillar.auth.jwt.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserDisabledException extends ResponseStatusException {
    public UserDisabledException(String username) {
        super(HttpStatus.FORBIDDEN, String.format("User %s is disabled", username));
    }

    public UserDisabledException(String username, Throwable e) {
        super(HttpStatus.FORBIDDEN, String.format("User %s is disabled", username), e);
    }
}
