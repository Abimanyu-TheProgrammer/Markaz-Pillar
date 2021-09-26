package com.markaz.pillar.auth.google.service;

import com.markaz.pillar.auth.google.advice.auth.annotation.Authenticated;
import com.markaz.pillar.auth.google.advice.limiter.annotation.Limited;
import com.markaz.pillar.auth.google.repository.model.GoogleToken;
import com.markaz.pillar.auth.google.service.model.CredentialResponse;
import com.markaz.pillar.auth.google.service.model.TokenResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.Collections;

@Service
public class GoogleOAuthService {
    private final String FQDN;

    @Value("${service.google.client.id}")
    private String clientId;

    @Value("${service.google.client.secret}")
    private String clientSecret;

    private RestTemplate authTemplate;
    private RestTemplate restTemplate;

    @SneakyThrows
    public GoogleOAuthService() {
        FQDN = InetAddress.getLocalHost().getCanonicalHostName();
    }

    @Autowired
    @Qualifier("googleAuthRestTemplate")
    public void setAuthTemplate(RestTemplate authTemplate) {
        this.authTemplate = authTemplate;
    }

    @Autowired
    @Qualifier("googleRestTemplate")
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Limited
    public TokenResponse getToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("code", code);
        data.add("grant_type", "authorization_code");
        data.add("client_id", clientId);
        data.add("client_secret", clientSecret);
        data.add("redirect_uri", FQDN);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(data, headers);
        return authTemplate.exchange("/token", HttpMethod.POST, entity, TokenResponse.class).getBody();
    }

    @Limited
    @Authenticated
    public CredentialResponse getCredentials(GoogleToken token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token.getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("/oauth2/v3/userinfo?alt={alt}",
                HttpMethod.GET, entity, CredentialResponse.class, "json").getBody();
    }

    public boolean checkToken(GoogleToken token, TokenType type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        switch (type) {
            case ACCESS_TOKEN:
                return authTemplate.exchange("/tokeninfo?access_token={token}",
                                HttpMethod.GET, entity, CredentialResponse.class, token.getAccessToken())
                        .getStatusCodeValue() == 200;
            case ID_TOKEN:
                return authTemplate.exchange("/tokeninfo?id_token={token}",
                                HttpMethod.GET, entity, CredentialResponse.class, token.getIdToken())
                        .getStatusCodeValue() == 200;
            default:
                throw new IllegalArgumentException("Not supported!");
        }
    }

    public TokenResponse refreshToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("refresh_token", refreshToken);
        data.add("grant_type", "refresh_token");
        data.add("client_id", clientId);
        data.add("client_secret", clientSecret);
        data.add("redirect_uri", FQDN);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(data, headers);
        return authTemplate.exchange("/token", HttpMethod.POST, entity, TokenResponse.class).getBody();
    }
}
