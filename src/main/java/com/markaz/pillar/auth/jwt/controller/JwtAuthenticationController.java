package com.markaz.pillar.auth.jwt.controller;


import com.markaz.pillar.auth.jwt.config.JwtTokenUtil;
import com.markaz.pillar.auth.jwt.controller.exception.InvalidAuthorizationException;
import com.markaz.pillar.auth.jwt.controller.exception.InvalidTokenException;
import com.markaz.pillar.auth.jwt.controller.exception.UserDisabledException;
import com.markaz.pillar.auth.jwt.controller.model.JwtRequest;
import com.markaz.pillar.auth.jwt.controller.model.JwtResponse;
import com.markaz.pillar.auth.jwt.controller.model.RefreshRequest;
import com.markaz.pillar.auth.jwt.controller.model.ValidateRequest;
import com.markaz.pillar.auth.jwt.repository.TokenRepository;
import com.markaz.pillar.auth.jwt.repository.model.AuthRefresh;
import com.markaz.pillar.auth.jwt.service.AuthenticationService;
import com.markaz.pillar.auth.jwt.service.JwtUserDetailsService;
import com.markaz.pillar.config.controller.model.annotation.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/authenticate")
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
    public String validateToken(@RequestBody @Valid ValidateRequest request) {
        Set<GrantedAuthority> claimAuthorities = new HashSet<>();
        for(String authority : request.getAuthority()) {
            claimAuthorities.add(new SimpleGrantedAuthority(authority));
        }

        String email = jwtTokenUtil.getEmailFromToken(request.getToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(!userDetails.isEnabled()) {
            throw new UserDisabledException(email);
        } else if(!userDetails.getAuthorities().containsAll(claimAuthorities)) {
            throw new InvalidAuthorizationException(email, claimAuthorities);
        }

        return "OK";
    }

    @PostMapping("/refresh")
    @ResponseMessage("Token is refreshed!")
    public JwtResponse refreshToken(@RequestBody RefreshRequest request) throws Exception {
        String email = jwtTokenUtil.getSubject(request.getAccessToken());

        Optional<AuthRefresh> optional = repository.findByRefreshToken(request.getRefreshToken());
        if(optional.isPresent()) {
            AuthRefresh refresh = optional.get();
            if(!refresh.isValid() || !refresh.getEmail().equals(email)) {
                throw new InvalidTokenException(request.getRefreshToken());
            }

            refresh.setValid(false);
            repository.save(refresh);

            return authenticationService.generateTokens(refresh.getEmail());
        } else {
            throw new InvalidTokenException(request.getRefreshToken());
        }
    }
}
