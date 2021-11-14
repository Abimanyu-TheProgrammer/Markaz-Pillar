package com.markaz.pillar.auth.google.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markaz.pillar.auth.google.repository.GoogleTokenRepository;
import com.markaz.pillar.auth.google.repository.model.GoogleToken;
import com.markaz.pillar.auth.google.service.GoogleOAuthService;
import com.markaz.pillar.auth.google.service.TokenType;
import com.markaz.pillar.auth.google.service.model.CredentialResponse;
import com.markaz.pillar.auth.google.service.model.TokenResponse;
import com.markaz.pillar.auth.jwt.controller.model.JwtResponse;
import com.markaz.pillar.auth.jwt.service.AuthenticationService;
import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.auth.service.RegistrationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.SecureRandom;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OAuthControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private OAuthController controller;

    @Mock
    private GoogleTokenRepository repository;

    @Mock
    private GoogleOAuthService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper mapper;

    public static MappingJackson2HttpMessageConverter createJacksonConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }


    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(createJacksonConverter())
                .build();
    }

    @Test
    void generateState() throws Exception {
        GoogleToken token = new GoogleToken();
        token.setState(
                RandomStringUtils.random(
                        30, 0, 0, true, true, null, new SecureRandom()
                )
        );

        when(repository.save(any(GoogleToken.class))).thenReturn(token);

        mockMvc.perform(post("/oauth/state")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(repository, times(1)).save(any());
    }

    @Test
    void generateToken() throws Exception {
        String code = "testCode";
        String state = "testState";

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

        CredentialResponse credentialResponse = mapper.readValue(
                "{\n" +
                        "    \"sub\": \"109664761410052735640\",\n" +
                        "    \"name\": \"Achmad Afriza Wibawa\",\n" +
                        "    \"given_name\": \"Achmad\",\n" +
                        "    \"family_name\": \"Afriza Wibawa\",\n" +
                        "    \"picture\": \"https://lh3.googleusercontent.com/a-/AOh14GhQX85aiZWcJoPe7Oj8G_0JntbgA_CLCbNtBMdW=s96-c\",\n" +
                        "    \"email\": \"achmad.afriza@ui.ac.id\",\n" +
                        "    \"email_verified\": true,\n" +
                        "    \"locale\": \"en-GB\",\n" +
                        "    \"hd\": \"ui.ac.id\"\n" +
                        "}", CredentialResponse.class
        );

        Mockito.when(service.getToken(anyString()))
                .thenReturn(response);
        Mockito.when(service.getCredentials(any(GoogleToken.class)))
                .thenReturn(credentialResponse);
        Mockito.when(repository.createToken(anyString(), anyString()))
                .thenReturn(new GoogleToken());

        mockMvc.perform(
                post("/oauth/token")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("state", state)
                        .content(String.format("{\"code\": \"%s\"}", code)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.credential.sub").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.credential.name").value(credentialResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.credential.email").value(credentialResponse.getEmail()));
    }

    @Test
    void createUserFromOAuth() throws Exception {
        String testEmail = "test.email@gmail.com";
        String testState = "testState";
        String request = "{\n" +
                "    \"email\" : \"test.email@gmail.com\",\n" +
                "    \"username\" : \"hazmi\",\n" +
                "    \"fullName\": \"wishnu hazmi lazuardi\",\n" +
                "    \"phoneNum\": \"08159774247\",\n" +
                "    \"password\": \"Admin123\",\n" +
                "    \"address\": \"Jl. Jakarta jakarta\"\n" +
                "}";

        GoogleToken token = new GoogleToken();
        token.setState(testState);
        Mockito.when(repository.getByState(testState))
                .thenReturn(token);
        Mockito.when(service.checkToken(token, TokenType.ACCESS_TOKEN))
                .thenReturn(true);

        CredentialResponse response = new CredentialResponse();
        response.setEmail(testEmail);
        Mockito.when(service.getCredentials(token))
                .thenReturn(response);

        Mockito.when(userRepository.findByEmail(testEmail))
                .thenReturn(Optional.empty());

        AuthUser user = mapper.readValue(request, AuthUser.class);
        Mockito.when(registrationService.register(any(AuthUser.class)))
                .thenReturn(user);

        Mockito.when(authenticationService.generateTokens(testEmail))
                .thenReturn(new JwtResponse("testAccess", "testRefresh"));

        mockMvc.perform(
                        post("/oauth/create")
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                                .queryParam("state", testState)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").exists());

        Mockito.verify(registrationService, atMostOnce()).register(any(AuthUser.class));
        Mockito.verify(authenticationService, atMostOnce()).authenticate(testEmail);
        Mockito.verify(authenticationService, atMostOnce()).generateTokens(testEmail);
    }

    @Test
    void createUserLogsIn_WhenEmailExists() throws Exception {
        String testEmail = "test.email@gmail.com";
        String testState = "testState";
        String request = "{\n" +
                "    \"email\" : \"test.email@gmail.com\",\n" +
                "    \"username\" : \"hazmi\",\n" +
                "    \"fullName\": \"wishnu hazmi lazuardi\",\n" +
                "    \"phoneNum\": \"08159774247\",\n" +
                "    \"password\": \"Admin123\",\n" +
                "    \"address\": \"Jl. Jakarta jakarta\"\n" +
                "}";

        GoogleToken token = new GoogleToken();
        token.setState(testState);
        Mockito.when(repository.getByState(testState))
                .thenReturn(token);
        Mockito.when(service.checkToken(token, TokenType.ACCESS_TOKEN))
                .thenReturn(true);

        CredentialResponse response = new CredentialResponse();
        response.setEmail(testEmail);
        Mockito.when(service.getCredentials(token))
                .thenReturn(response);

        AuthUser user = mapper.readValue(request, AuthUser.class);
        Mockito.when(userRepository.findByEmail(testEmail))
                .thenReturn(Optional.of(user));

        Mockito.when(authenticationService.generateTokens(testEmail))
                .thenReturn(new JwtResponse("testAccess", "testRefresh"));

        mockMvc.perform(
                        post("/oauth/create")
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                                .queryParam("state", testState)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").exists());

        Mockito.verify(registrationService, never()).register(any(AuthUser.class));
        Mockito.verify(authenticationService, atMostOnce()).authenticate(testEmail);
        Mockito.verify(authenticationService, atMostOnce()).generateTokens(testEmail);
    }
}