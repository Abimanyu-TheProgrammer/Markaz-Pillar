package com.markaz.pillar.auth.controller;

import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.auth.service.AuthService;
import com.markaz.pillar.config.controller.model.annotation.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@PreAuthorize("permitAll()")
public class AuthController {
    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseMessage("AuthUser Successfully Registered")
    public void register(@Valid @RequestBody AuthUser authUser){
        authService.register(authUser);
    }
}
