package com.markaz.pillar.auth.jwt.controller.exception;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

public class InvalidAuthorizationException extends ResponseStatusException {
    public InvalidAuthorizationException(String username, Collection<? extends GrantedAuthority> authorities) {
        super(HttpStatus.FORBIDDEN, String.format("%s is not authorized for %s", username, new Gson().toJson(authorities)));
    }

    public InvalidAuthorizationException(String username, Collection<? extends GrantedAuthority> authorities, Throwable e) {
        super(HttpStatus.FORBIDDEN, String.format("%s is not authorized for %s", username, new Gson().toJson(authorities)), e);
    }
}
