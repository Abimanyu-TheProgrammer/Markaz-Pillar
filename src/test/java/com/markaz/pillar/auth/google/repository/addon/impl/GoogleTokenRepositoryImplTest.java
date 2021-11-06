package com.markaz.pillar.auth.google.repository.addon.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markaz.pillar.auth.google.repository.GoogleTokenRepository;
import com.markaz.pillar.auth.google.repository.model.GoogleToken;
import com.markaz.pillar.auth.google.service.GoogleOAuthService;
import com.markaz.pillar.auth.google.service.model.TokenResponse;
import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GoogleTokenRepositoryImplTest {
    @InjectMocks
    private GoogleTokenRepositoryImpl repositoryImpl;

    @Mock
    private GoogleOAuthService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    @Lazy
    private GoogleTokenRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void refreshToken() {
        GoogleToken oldToken = new GoogleToken();
        oldToken.setRefreshToken("test");

        AuthUser user = Mockito.mock(AuthUser.class);
        Mockito.when(user.getToken())
                .thenReturn(oldToken);
        Mockito.when(user.getId())
                .thenReturn(1);

        TokenResponse response = new TokenResponse();
        response.setAccessToken("testToken");
        response.setRefreshToken("testRefresh");
        response.setExpiresIn(12345);

        Mockito.when(service.refreshToken("test"))
                .thenReturn(response);

        GoogleToken newToken = repositoryImpl.refreshToken(user);

        Mockito.verify(service, Mockito.times(1)).refreshToken("test");
        Mockito.verify(user, Mockito.atLeastOnce()).setToken(newToken);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(repository, Mockito.times(1)).deleteExistingTokens(1);

        assertThat(newToken)
                .hasFieldOrPropertyWithValue("accessToken", response.getAccessToken())
                .hasFieldOrPropertyWithValue("refreshToken", response.getRefreshToken());
    }

    @Test
    void testIfTokenIsNull() {
        GoogleToken oldToken = new GoogleToken();
        oldToken.setRefreshToken("test");

        Mockito.when(repository.findFirstByAccountIdOrderByCreatedAtDesc(1))
                .thenReturn(oldToken);

        AuthUser user = Mockito.mock(AuthUser.class);
        Mockito.when(user.getId())
                .thenReturn(1);

        TokenResponse response = new TokenResponse();
        response.setAccessToken("testToken");
        response.setRefreshToken("testRefresh");
        response.setExpiresIn(12345);

        Mockito.when(service.refreshToken("test"))
                .thenReturn(response);

        GoogleToken newToken = repositoryImpl.refreshToken(user);

        Mockito.verify(service, Mockito.times(1)).refreshToken("test");
        Mockito.verify(user, Mockito.atLeastOnce()).setToken(newToken);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(repository, Mockito.times(1)).deleteExistingTokens(1);

        assertThat(newToken)
                .hasFieldOrPropertyWithValue("accessToken", response.getAccessToken())
                .hasFieldOrPropertyWithValue("refreshToken", response.getRefreshToken());
    }

    @Test
    void createToken() throws JsonProcessingException {
        String state = "testState";
        String code = "testCode";

        GoogleToken existing = new GoogleToken();
        existing.setId(1);
        existing.setState(state);

        Mockito.when(repository.getByState(state))
                .thenReturn(existing);

        TokenResponse response = mapper.readValue(
                "{\n" +
                        "    \"access_token\": \"zzzz.a0ARrdaM8_onP8-EUu59H59ZIh9cPbTnWyRtlBbztim56YwcplSgT14vz269AR7aruQV1V8TViS6za0nOLK0aBkiZUqFtT26sWvzV0buQ6tiJ-HlRrW3EVrshNPOfglYKy6s-1rj-a5ozN-CdPUE6Q92t1693x\",\n" +
                        "    \"expires_in\": 3598,\n" +
                        "    \"refresh_token\": \"testRefresh\",\n" +
                        "    \"scope\": \"https://www.googleapis.com/auth/userinfo.profile openid https://www.googleapis.com/auth/userinfo.email\",\n" +
                        "    \"token_type\": \"Bearer\",\n" +
                        "    \"id_token\": \"zzzzbGciOiJSUzI1NiIsImtpZCI6ImMzMTA0YzY4OGMxNWU2YjhlNThlNjdhMzI4NzgwOTUyYjIxNzQwMTciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI2MjA4MjAyNjI4NzctODVmOWFudWdtdTc3ZjU5aWJ0dTNxZmJmMm5tYXQwMGouYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2MjA4MjAyNjI4NzctODVmOWFudWdtdTc3ZjU5aWJ0dTNxZmJmMm5tYXQwMGouYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDk2NjQ3NjE0MTAwNTI3MzU2NDAiLCJoZCI6InVpLmFjLmlkIiwiZW1haWwiOiJhY2htYWQuYWZyaXphQHVpLmFjLmlkIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJJaGN5MXZ0LVdtWjZ6MFFGeVh4OGtRIiwibmFtZSI6IkFjaG1hZCBBZnJpemEgV2liYXdhIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hLS9BT2gxNEdoUVg4NWFpWldjSm9QZTdPajhHXzBKbnRiZ0FfQ0xDYk50Qk1kVz1zOTYtYyIsImdpdmVuX25hbWUiOiJBY2htYWQiLCJmYW1pbHlfbmFtZSI6IkFmcml6YSBXaWJhd2EiLCJsb2NhbGUiOiJlbi1HQiIsImlhdCI6MTYzMjM5MzYyMSwiZXhwIjoxNjMyMzk3MjIxfQ.dH1LApAP1TkCY5-R_9fYLXbcQkl6Wi6OtuXEBhmsE8xF-MM9hoYBankRz3oOaqqenxftbTyvQe7qk-dPuYAgaaNu12yyPTrmcd4GQoezkAinJoLtsjmz3a2Nt1F2xU61oZeOHmbm-P0sC8uiVev4ftmP3lwFw-VdGvckSW3-4-LW1h5E5zT5tnE9WV-fA5ZacxxxqjfTN3HjjxLQutGHUEhklvij0Rpw_h0Ao4g_uwwSwpdR5HQeKMyNYq76hGitSNJjaIe8nZDGO5boq1O0ZzaeNYViTFww-kAJQjLvr71Ra0jCJyb9UYrNovPIEblrDBIKYRWb3Ux50tsqntB6rA\"\n" +
                        "}", TokenResponse.class
        );

        Mockito.when(service.getToken(code))
                .thenReturn(response);
        Mockito.when(repository.save(existing))
                .thenReturn(existing);

        GoogleToken result = repositoryImpl.createToken(state, code);

        Mockito.verify(repository, Mockito.times(1)).save(existing);
        Mockito.verify(service, Mockito.times(1)).getToken(code);
        assertThat(existing)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "account", "expireAt")
                .isEqualTo(result);
    }
}