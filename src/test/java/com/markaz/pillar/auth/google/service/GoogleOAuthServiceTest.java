package com.markaz.pillar.auth.google.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markaz.pillar.auth.google.repository.model.GoogleToken;
import com.markaz.pillar.auth.google.service.model.CredentialResponse;
import com.markaz.pillar.auth.google.service.model.TokenResponse;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
class GoogleOAuthServiceTest {
    @Autowired
    private GoogleOAuthService service;
    @Autowired
    private ObjectMapper mapper;

    private RestTemplate authTemplate;
    private RestTemplate restTemplate;

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

    @Test
    void getToken() throws JsonProcessingException {
        TokenResponse response = new TokenResponse();

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(authTemplate);
        mockServer.expect(
                        ExpectedCount.once(),
                        requestTo(Matchers.endsWith("/token"))
                )
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(content().string(Matchers.containsString("client_id")))
                .andExpect(content().string(Matchers.containsString("client_secret")))
                .andExpect(content().string(Matchers.containsString("redirect_uri")))
                .andExpect(content().string(Matchers.containsString("grant_type")))
                .andExpect(content().string(Matchers.containsString("code")))
                .andRespond(
                        withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(response))
                );


        TokenResponse ans = service.getToken("test");

        mockServer.verify();
        Assertions.assertThat(ans).isEqualTo(response);
    }

    @Test
    void getCredentials() throws JsonProcessingException {
        CredentialResponse response = new CredentialResponse();

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(
                        ExpectedCount.once(),
                        requestTo(Matchers.containsString("/oauth2/v3/userinfo"))
                )
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("alt", "json"))
                .andRespond(
                        withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(response))
                );

        GoogleToken token = Mockito.mock(GoogleToken.class);
        Mockito.when(token.getExpireAt()).thenReturn(LocalDateTime.now().plusSeconds(3600));
        Mockito.when(token.getAccessToken()).thenReturn("test");

        CredentialResponse ans = service.getCredentials(token);

        mockServer.verify();
        Mockito.verify(token, Mockito.atLeastOnce()).getAccessToken();
        Assertions.assertThat(ans).isEqualTo(response);
    }

    @Test
    void checkTokenIdToken() throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(authTemplate);
        mockServer.expect(
                        ExpectedCount.once(),
                        requestTo(Matchers.containsString("/tokeninfo"))
                )
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("id_token", Matchers.any(String.class)))
                .andRespond(
                        withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(response))
                );

        GoogleToken token = Mockito.mock(GoogleToken.class);
        boolean ans = service.checkToken(token, TokenType.ID_TOKEN);

        mockServer.verify();
        Mockito.verify(token, Mockito.atLeastOnce()).getIdToken();
        Assertions.assertThat(ans).isTrue();
    }

    @Test
    void checkTokenAccessToken() throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(authTemplate);
        mockServer.expect(
                        ExpectedCount.once(),
                        requestTo(Matchers.containsString("/tokeninfo"))
                )
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("access_token", Matchers.any(String.class)))
                .andRespond(
                        withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(response))
                );

        GoogleToken token = Mockito.mock(GoogleToken.class);
        boolean ans = service.checkToken(token, TokenType.ACCESS_TOKEN);

        mockServer.verify();
        Mockito.verify(token, Mockito.atLeastOnce()).getAccessToken();
        Assertions.assertThat(ans).isTrue();
    }

    @Test
    void refreshToken() throws JsonProcessingException {
        TokenResponse response = new TokenResponse();

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(authTemplate);
        mockServer.expect(
                        ExpectedCount.once(),
                        requestTo(Matchers.endsWith("/token"))
                )
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(content().string(Matchers.containsString("client_id")))
                .andExpect(content().string(Matchers.containsString("client_secret")))
                .andExpect(content().string(Matchers.containsString("redirect_uri")))
                .andExpect(content().string(Matchers.containsString("grant_type")))
                .andExpect(content().string(Matchers.containsString("refresh_token")))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(response))
                );


        TokenResponse ans = service.refreshToken("test");

        mockServer.verify();
        Assertions.assertThat(ans).isEqualTo(response);
    }
}