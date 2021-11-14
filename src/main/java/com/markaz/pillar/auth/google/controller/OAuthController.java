package com.markaz.pillar.auth.google.controller;

import com.markaz.pillar.auth.controller.model.RegistrationRequestDTO;
import com.markaz.pillar.auth.google.controller.model.TokenRequest;
import com.markaz.pillar.auth.google.repository.GoogleTokenRepository;
import com.markaz.pillar.auth.google.repository.model.GoogleToken;
import com.markaz.pillar.auth.google.service.GoogleOAuthService;
import com.markaz.pillar.auth.google.service.TokenType;
import com.markaz.pillar.auth.google.service.model.CredentialResponse;
import com.markaz.pillar.auth.jwt.controller.model.JwtResponse;
import com.markaz.pillar.auth.jwt.service.AuthenticationService;
import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.auth.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/oauth")
@PreAuthorize("permitAll()")
@Slf4j
public class OAuthController {
    @Value("${service.jwt.state.length}")
    private int stateExpire;

    private GoogleTokenRepository repository;
    private GoogleOAuthService service;
    private UserRepository userRepository;
    private AuthenticationService authenticationService;
    private RegistrationService registrationService;

    @Autowired
    public void setAuthService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
    public ResponseEntity<TokenResponse> generateToken(@RequestParam String state,
                                           @RequestBody @Valid TokenRequest request) throws NoSuchAlgorithmException {
        GoogleToken token = repository.createToken(state, request.getCode());
        CredentialResponse response = service.getCredentials(token);

        Optional<AuthUser> optional = userRepository.findByEmail(response.getEmail());
        if(optional.isPresent()) {
            AuthUser user = optional.get();
            token.setState(null);
            user.setToken(token);
            userRepository.save(user);

            authenticationService.authenticate(user.getEmail());

            return ResponseEntity.ok(
                    TokenResponse.builder()
                            .token(authenticationService.generateTokens(user.getEmail()))
                            .build()
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                TokenResponse.builder()
                        .credential(response)
                        .build()
        );
    }

    @PostMapping(value = "/create", params = {"state"})
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse createUserFromOAuth(@RequestParam String state,
                                           @RequestBody @Valid RegistrationRequestDTO request)
            throws NoSuchAlgorithmException {
        GoogleToken token = repository.getByState(state);
        if(!service.checkToken(token, TokenType.ACCESS_TOKEN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad State!");
        }

        CredentialResponse credential = service.getCredentials(token);
        if(!credential.getEmail().equals(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad State!");
        }

        AuthUser user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    AuthUser temp = registrationService.register(request.mapTo());

                    token.setState(null);
                    token.setAccount(temp);
                    repository.save(token);

                    return temp;
                });

        authenticationService.authenticate(user.getEmail());

        return authenticationService.generateTokens(request.getEmail());
    }
}
