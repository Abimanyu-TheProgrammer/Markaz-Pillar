package com.markaz.pillar.auth.jwt.controller;


import com.markaz.pillar.auth.jwt.config.JwtTokenUtil;
import com.markaz.pillar.auth.jwt.controller.exception.InvalidAuthorizationException;
import com.markaz.pillar.auth.jwt.controller.exception.InvalidCredentialsException;
import com.markaz.pillar.auth.jwt.controller.exception.InvalidTokenException;
import com.markaz.pillar.auth.jwt.controller.exception.UserDisabledException;
import com.markaz.pillar.auth.jwt.controller.model.JwtRequest;
import com.markaz.pillar.auth.jwt.controller.model.JwtResponse;
import com.markaz.pillar.auth.jwt.controller.model.RefreshRequest;
import com.markaz.pillar.auth.jwt.controller.model.ValidateRequest;
import com.markaz.pillar.auth.jwt.repository.TokenRepository;
import com.markaz.pillar.auth.jwt.repository.model.AuthRefresh;
import com.markaz.pillar.auth.jwt.service.JwtUserDetailsService;
import com.markaz.pillar.config.controller.model.annotation.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/authenticate")
public class JwtAuthenticationController {
    @Value("${service.jwt.refresh.length}")
    private long refreshLength;

    private TokenRepository repository;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsService userDetailsService;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        return generateTokens(authenticationRequest.getEmail());
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

    private JwtResponse generateTokens(String email) throws NoSuchAlgorithmException {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String token = jwtTokenUtil.generateToken(userDetails);

        byte[] salt = KeyGenerators.secureRandom(48).generateKey();
        final String refresh = jwtTokenUtil.generateRefreshToken(userDetails, salt);

        AuthRefresh authRefresh = new AuthRefresh();
        authRefresh.setEmail(userDetails.getUsername());
        authRefresh.setSalt(Base64.getEncoder().encodeToString(salt));
        authRefresh.setRefreshToken(refresh);
        authRefresh.setExpireAt(LocalDateTime.now().plusSeconds(refreshLength));

        repository.setAllValidFalseForUsername(email);
        repository.save(authRefresh);

        return new JwtResponse(token, refresh);
    }

    private void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new UserDisabledException(email, e);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException(email, e);
        }
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

            return generateTokens(refresh.getEmail());
        } else {
            throw new InvalidTokenException(request.getRefreshToken());
        }
    }
}
