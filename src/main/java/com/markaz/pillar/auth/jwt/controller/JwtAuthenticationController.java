package com.markaz.pillar.auth.jwt.controller;


import com.google.gson.Gson;
import com.markaz.pillar.auth.jwt.config.JwtTokenUtil;
import com.markaz.pillar.auth.jwt.controller.model.JwtRequest;
import com.markaz.pillar.auth.jwt.controller.model.JwtResponse;
import com.markaz.pillar.auth.jwt.controller.model.RefreshRequest;
import com.markaz.pillar.auth.jwt.controller.model.ValidateRequest;
import com.markaz.pillar.auth.jwt.repository.TokenRepository;
import com.markaz.pillar.auth.jwt.repository.model.AuthRefresh;
import com.markaz.pillar.auth.jwt.service.AuthenticationService;
import com.markaz.pillar.auth.jwt.service.JwtUserDetailsService;
import com.markaz.pillar.config.controller.model.annotation.ResponseMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/authenticate")
@Slf4j
public class JwtAuthenticationController {
    private TokenRepository repository;
    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsService userDetailsService;
    private AuthenticationService authenticationService;

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setUserDetailsService(JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setRepository(TokenRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseMessage("User is authenticated!")
    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws NoSuchAlgorithmException {
        authenticationService.authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        return authenticationService.generateTokens(authenticationRequest.getEmail());
    }

    @PostMapping("/validate")
    @ResponseMessage("Token is Valid!")
    public void validateToken(@RequestBody @Valid ValidateRequest request) {
        Set<GrantedAuthority> claimAuthorities = new HashSet<>();
        for(String authority : request.getAuthority()) {
            claimAuthorities.add(new SimpleGrantedAuthority(authority));
        }

        try {
            String email = jwtTokenUtil.getEmailFromToken(request.getToken());
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if(!userDetails.isEnabled()) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        String.format("User %s is disabled", email)
                );
            } else if(!userDetails.getAuthorities().containsAll(claimAuthorities)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        String.format("%s is not authorized for %s", email, new Gson().toJson(claimAuthorities))
                );
            }
        } catch (MalformedJwtException | SignatureException e) {
            log.error(String.format("Validation Attempt, Token %s is malformed!", request.getToken()), e);

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token is malformed!");
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token has expired!");
        }
    }

    @PostMapping("/refresh")
    @ResponseMessage("Token is refreshed!")
    public JwtResponse refreshToken(@RequestBody RefreshRequest request) throws NoSuchAlgorithmException {
        String email = jwtTokenUtil.getSubject(request.getAccessToken());

        Optional<AuthRefresh> optional = repository.findByRefreshToken(request.getRefreshToken());
        if(optional.isPresent()) {
            AuthRefresh refresh = optional.get();
            if(!refresh.isValid() || !refresh.getEmail().equals(email)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("Invalid token %s", request.getRefreshToken())
                );
            }

            refresh.setValid(false);
            repository.save(refresh);

            return authenticationService.generateTokens(refresh.getEmail());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Invalid token %s", request.getRefreshToken())
            );
        }
    }
}
