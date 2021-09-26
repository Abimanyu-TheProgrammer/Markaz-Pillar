package com.markaz.pillar.auth.google.repository.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markaz.pillar.auth.google.repository.GoogleTokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GoogleTokenTest {
    private GoogleToken token;

    @Autowired
    private GoogleTokenRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUpEntity() {
        String state = "lalalalala";
        String accessToken = "ya29.a0ARrdaM8_onP8-EUu59H59ZIh9cPbTnWyRtlBbztim56YwcplSgT14vz269AR7aruQV1V8TViS6za0nOLK0aBkiZUqFtT26sWvzV0buQ6tiJ-HlRrW3EVrshNPOfglYKy6s-1rj-a5ozN-CdPUE6Q92t1694x";
        String refreshToken = "1//0g0hJIQkak_X3CgYIARAAGBASNwF-L9Ir561fw7X8CP7JDeqBmXBlcarRFeXFpIHwVRdkx41QuuDNZY5qqppdcfI5yrZ7inAeDM1";
        String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImMzMTA0YzY4OGMxNWU2YjhlNThlNjdhMzI4NzgwOTUyYjIxNzQwMTciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI2MjA4MjAyNjI4NzctODVmOWFudWdtdTc3ZjU5aWJ0dTNxZmJmMm5tYXQwMGouYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2MjA4MjAyNjI4NzctODVmOWFudWdtdTc3ZjU5aWJ0dTNxZmJmMm5tYXQwMGouYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDk2NjQ3NjE0MTAwNTI3MzU2NDAiLCJoZCI6InVpLmFjLmlkIiwiZW1haWwiOiJhY2htYWQuYWZyaXphQHVpLmFjLmlkIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJJaGN5MXZ0LVdtWjZ6MFFGeVh4OGtRIiwibmFtZSI6IkFjaG1hZCBBZnJpemEgV2liYXdhIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hLS9BT2gxNEdoUVg4NWFpWldjSm9QZTdPajhHXzBKbnRiZ0FfQ0xDYk50Qk1kVz1zOTYtYyIsImdpdmVuX25hbWUiOiJBY2htYWQiLCJmYW1pbHlfbmFtZSI6IkFmcml6YSBXaWJhd2EiLCJsb2NhbGUiOiJlbi1HQiIsImlhdCI6MTYzMjM5MzYyMSwiZXhwIjoxNjMyMzk3MjIxfQ.dH1LApAP1TkCY5-R_9fYLXbcQkl6Wi6OtuXEBhmsE8xF-MM9hoYBankRz3oOaqqenxftbTyvQe7qk-dPuYAgaaNu12yyPTrmcd4GQoezkAinJoLtsjmz3a2Nt1F2xU61oZeOHmbm-P0sC8uiVev4ftmP3lwFw-VdGvckSW3-4-LW1h5E5zT5tnE9WV-fA5ZacxxxqjfTN3HjjxLQutGHUEhklvij0Rpw_h0Ao4g_uwwSwpdR5HQeKMyNYq76hGitSNJjaIe8nZDGO5boq1O0ZzaeNYViTFww-kAJQjLvr71Ra0jCJyb9UYrNovPIEblrDBIKYRWb3Ux50tsqntB6rA";
        LocalDateTime expire = LocalDateTime.now().plusSeconds(3600);

        this.token = new GoogleToken();
        this.token.setState(state);
        this.token.setExpireAt(expire);
        this.token.setAccessToken(accessToken);
        this.token.setRefreshToken(refreshToken);
        this.token.setIdToken(idToken);
    }

    @Test
    void testPersistGoogleTokenEntity() {
        GoogleToken googleToken = repository.save(token);
        Optional<GoogleToken> found = repository.findById(token.getId());

        Assertions.assertThat(found)
                .isNotEmpty();
        Assertions.assertThat(googleToken)
                .usingRecursiveComparison()
                .isEqualTo(found.get());
    }

    @Test
    void testGoogleTokenFieldsIgnored() throws JsonProcessingException {
        String json = mapper.writeValueAsString(token);
        Assertions.assertThat(json)
                .doesNotContainIgnoringCase("accessToken", "refreshToken", "idToken");
    }
}