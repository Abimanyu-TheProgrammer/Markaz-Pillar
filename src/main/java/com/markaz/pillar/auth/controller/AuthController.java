package com.markaz.pillar.auth.controller;

import com.markaz.pillar.auth.controller.model.RegistrationRequestDTO;
import com.markaz.pillar.auth.jwt.controller.model.JwtResponse;
import com.markaz.pillar.auth.jwt.service.AuthenticationService;
import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.auth.service.RegistrationService;
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
import java.security.NoSuchAlgorithmException;

@RestController
@PreAuthorize("permitAll()")
public class AuthController {
    private RegistrationService registrationService;
    private AuthenticationService authenticationService;

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Autowired
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseMessage("AuthUser Successfully Registered")
    public JwtResponse register(@RequestBody @Valid RegistrationRequestDTO requestDTO) throws NoSuchAlgorithmException {
        AuthUser user = registrationService.register(requestDTO.mapTo());

        authenticationService.authenticate(user.getEmail());
        return authenticationService.generateTokens(user.getEmail());
    }
}
