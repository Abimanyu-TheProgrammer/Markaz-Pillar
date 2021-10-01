package com.markaz.pillar.auth.google.controller;

import com.markaz.pillar.auth.google.controller.model.TokenRequest;
import com.markaz.pillar.auth.google.repository.GoogleTokenRepository;
import com.markaz.pillar.auth.google.repository.model.GoogleToken;
import com.markaz.pillar.auth.google.service.GoogleOAuthService;
import com.markaz.pillar.auth.google.service.model.CredentialResponse;
import com.markaz.pillar.auth.repository.models.AuthUser;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/oauth")
@PreAuthorize("permitAll()")
public class OAuthController {
    @Value("${service.jwt.state.length}")
    private int stateExpire;

    private GoogleTokenRepository repository;
    private GoogleOAuthService service;

    @Autowired
    public void setService(GoogleOAuthService service) {
        this.service = service;
    }

    @Autowired
    public void setRepository(GoogleTokenRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/state")
    @ResponseStatus(HttpStatus.CREATED)
    public GoogleToken generateState() {
        GoogleToken token = new GoogleToken();
        token.setState(
                RandomStringUtils.random(
                30, 0, 0, true, true, null, new SecureRandom()
                )
        );
        token.setExpireAt(LocalDateTime.now().plusSeconds(stateExpire));

        return repository.save(token);
    }

    @PostMapping(value = "/token", params = {"state"})
    @ResponseStatus(HttpStatus.CREATED)
    public CredentialResponse generateToken(@RequestParam String state, @RequestBody @Valid TokenRequest request) {
        GoogleToken token = repository.createToken(state, request.getCode());

        return service.getCredentials(token);
    }

    @PostMapping(value = "/create", params = {"state"})
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUser createUserFromOAuth(@RequestParam String state, @RequestBody AuthUser request) {
        throw new NotImplementedException();
    }
}
