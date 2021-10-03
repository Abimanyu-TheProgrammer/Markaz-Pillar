package com.markaz.pillar.auth.jwt.service;

import com.markaz.pillar.auth.jwt.config.JwtTokenUtil;
import com.markaz.pillar.auth.jwt.controller.exception.InvalidCredentialsException;
import com.markaz.pillar.auth.jwt.controller.exception.UserDisabledException;
import com.markaz.pillar.auth.jwt.controller.model.JwtResponse;
import com.markaz.pillar.auth.jwt.repository.TokenRepository;
import com.markaz.pillar.auth.jwt.repository.model.AuthRefresh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class AuthenticationService {
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

    public JwtResponse generateTokens(String email) throws NoSuchAlgorithmException {
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

    public void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new UserDisabledException(email, e);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException(email, e);
        }
    }

    public void authenticate(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(!userDetails.isEnabled()) {
            throw new UserDisabledException(userDetails.getUsername());
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
