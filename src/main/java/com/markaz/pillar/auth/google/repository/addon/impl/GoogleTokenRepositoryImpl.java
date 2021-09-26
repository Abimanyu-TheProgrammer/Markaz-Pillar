package com.markaz.pillar.auth.google.repository.addon.impl;

import com.markaz.pillar.auth.google.repository.GoogleTokenRepository;
import com.markaz.pillar.auth.google.repository.addon.GoogleTokenAddOn;
import com.markaz.pillar.auth.google.repository.model.GoogleToken;
import com.markaz.pillar.auth.google.service.GoogleOAuthService;
import com.markaz.pillar.auth.google.service.model.TokenResponse;
import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class GoogleTokenRepositoryImpl implements GoogleTokenAddOn {
    private GoogleOAuthService oAuthService;
    private GoogleTokenRepository repository;
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setOAuthService(GoogleOAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Autowired
    @Lazy
    public void setRepository(GoogleTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public GoogleToken refreshToken(AuthUser user) {
        GoogleToken token = Optional.ofNullable(user.getToken())
                .orElse(repository.findFirstByAccount_IdOrderByCreatedAtDesc(user.getId()));

        LocalDateTime currentTimestamp = LocalDateTime.now();
        TokenResponse tokenResponse = oAuthService.refreshToken(token.getRefreshToken());
        GoogleToken refreshedToken = new GoogleToken();
        refreshedToken.setState(token.getState());
        refreshedToken.setAccessToken(tokenResponse.getAccessToken());
        refreshedToken.setRefreshToken(tokenResponse.getRefreshToken());
        refreshedToken.setExpireAt(currentTimestamp.plusSeconds(tokenResponse.getExpiresIn()));

        user.setToken(refreshedToken);

        repository.deleteExistingTokens(user.getId());
        userRepository.save(user);

        return refreshedToken;
    }

    @Override
    public GoogleToken createToken(String state, String code) {
        GoogleToken existing = Optional.ofNullable(repository.getByState(state))
                .orElseThrow(() -> new IllegalArgumentException("State doesn't exist"));

        LocalDateTime created = LocalDateTime.now();
        TokenResponse response = oAuthService.getToken(code);

        existing.setAccessToken(response.getAccessToken());
        existing.setRefreshToken(response.getRefreshToken());
        existing.setIdToken(response.getIdToken());
        existing.setCreatedAt(created.plusSeconds(response.getExpiresIn()));

        return repository.save(existing);
    }
}
