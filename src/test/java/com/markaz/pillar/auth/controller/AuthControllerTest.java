package com.markaz.pillar.auth.controller;

import com.google.gson.Gson;
import com.markaz.pillar.PillarBeApplication;
import com.markaz.pillar.auth.jwt.config.JwtTokenUtil;
import com.markaz.pillar.auth.jwt.service.JwtUserDetailsService;
import com.markaz.pillar.auth.repository.RoleRepository;
import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.auth.repository.models.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "jwt.secret=markaz")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = PillarBeApplication.class)
class AuthControllerTest {
    private MockMvc mockMvc;
    private ApplicationContext context;
    private RoleRepository roleRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private JwtUserDetailsService userDetailsService;

    private final Gson gson = new Gson();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup((WebApplicationContext) context)
                .apply(springSecurity())
                .build();

        Role role = new Role();
        role.setId(1);
        role.setName("MEMBER");

        roleRepository.saveAndFlush(role);
    }

    @Test
    void testRegisterIfInputIsAllCorrect() throws Exception {
        AuthUser authUser = new AuthUser();
        authUser.setEmail("jarfix01@gmail.com");
        authUser.setUsername("mofo01");
        authUser.setFullName("Abimanyu Yuda Dewa");
        authUser.setPhoneNum("085846001404");
        authUser.setAddress("di Jalan");
        authUser.setPassword("1234567a");

        mockMvc.perform(post("/register")
                        .contentType("application/json")
                        .content(gson.toJson(authUser))
                        .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testRegisterIfOneFieldIsMissing() throws Exception {
        AuthUser authUser = new AuthUser();
        authUser.setUsername("mofo01");
        authUser.setFullName("Abimanyu Yuda Dewa");
        authUser.setPhoneNum("085846001404");
        authUser.setAddress("di Jalan");
        authUser.setPassword("1234567a");

        mockMvc.perform(post("/register")
                .contentType("application/json")
                .content(gson.toJson(authUser))
                .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterIfAllInputIsBlank() throws Exception {
        AuthUser authUser = new AuthUser();
        authUser.setUsername("");
        authUser.setFullName("");
        authUser.setPhoneNum("");
        authUser.setAddress("");
        authUser.setPassword("");

        mockMvc.perform(post("/register")
                .contentType("application/json")
                .content(gson.toJson(authUser))
                .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterIfPasswordDoesntHave8Chars() throws Exception {
        AuthUser authUser = new AuthUser();
        authUser.setEmail("jarfix01@gmail.com");
        authUser.setUsername("mofo01");
        authUser.setFullName("Abimanyu Yuda Dewa");
        authUser.setPhoneNum("085846001404");
        authUser.setAddress("di Jalan");
        authUser.setPassword("123");

        mockMvc.perform(post("/register")
                .contentType("application/json")
                .content(gson.toJson(authUser))
                .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterIfPasswordDoensntHaveNumbers() throws Exception {
        AuthUser authUser = new AuthUser();
        authUser.setEmail("jarfix01@gmail.com");
        authUser.setUsername("mofo01");
        authUser.setFullName("Abimanyu Yuda Dewa");
        authUser.setPhoneNum("085846001404");
        authUser.setAddress("di Jalan");
        authUser.setPassword("abcdefghij");

        mockMvc.perform(post("/register")
                .contentType("application/json")
                .content(gson.toJson(authUser))
                .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterIfPasswordDoensntHaveLetters() throws Exception {
        AuthUser authUser = new AuthUser();
        authUser.setEmail("jarfix01@gmail.com");
        authUser.setUsername("mofo01");
        authUser.setFullName("Abimanyu Yuda Dewa");
        authUser.setPhoneNum("085846001404");
        authUser.setAddress("di Jalan");
        authUser.setPassword("12345678");

        mockMvc.perform(post("/register")
                .contentType("application/json")
                .content(gson.toJson(authUser))
                .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testValidLogin() throws Exception {
        AuthUser authUser = new AuthUser();
        Role role = new Role();
        role.setName("SUPERUSER");
        authUser.setEmail("jarfix01@gmail.com");
        authUser.setRole(role);
        authUser.setUsername("mofo01");
        authUser.setFullName("Abimanyu Yuda Dewa");
        authUser.setPhoneNum("085846001404");
        authUser.setAddress("di Jalan");
        authUser.setPassword("1234567a");

        Mockito.when(userRepository.findByEmail("jarfix01@gmail.com")).thenReturn(Optional.of(authUser));


        jwtTokenUtil.setJwtSecret(jwtSecret);
        jwtTokenUtil.setTokenLength(1000);
        UserDetails userDetails = userDetailsService.loadUserByUsername("jarfix01@gmail.com");
        String token = jwtTokenUtil.generateToken(userDetails);

        Assertions.assertTrue(jwtTokenUtil.validateToken(token, userDetails));
    }

    @Test
    void testInvalidLogin() throws Exception {
        AuthUser invalidLogIn = new AuthUser();
        invalidLogIn.setPassword("Gibberish");
        invalidLogIn.setEmail("jarfix01@gmail.com");

        mockMvc.perform(post("/authenticate")
                        .contentType("application/json")
                        .content(gson.toJson(invalidLogIn))
                        .characterEncoding("utf-8")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
