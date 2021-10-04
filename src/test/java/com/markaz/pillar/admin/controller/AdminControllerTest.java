package com.markaz.pillar.admin.controller;

import com.markaz.pillar.PillarBeApplication;
import com.markaz.pillar.admin.service.AdminService;
import com.markaz.pillar.auth.jwt.config.JwtTokenUtil;
import com.markaz.pillar.auth.jwt.service.JwtUserDetailsService;
import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = PillarBeApplication.class)
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    private MockMvc mockMvc;
    private ApplicationContext context;
    private AuthUser authUser;
    private String token;

    @MockBean
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtUserDetailsService userDetailsService;

    @MockBean
    private AdminService adminService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup((WebApplicationContext) context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(authorities = "CRUD_USERS")
    void testIfGetAllUsersRouteWorks() throws Exception {

        List<AuthUser> res = new ArrayList<>();
        res.add(authUser);
        Mockito.when(adminService.getAllUsers()).thenReturn(res);

        mockMvc.perform(get("/all-users")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testIfGetAllUsersRouteReturnsStatus403() throws Exception {

        List<AuthUser> res = new ArrayList<>();
        res.add(authUser);
        Mockito.when(adminService.getAllUsers()).thenReturn(res);

        mockMvc.perform(get("/all-users")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "CRUD_USERS")
    void testIfGetSingleUserRouteWorks() throws Exception {
        Mockito.when(adminService.getUser("jarfix01@gmail.com")).thenReturn(authUser);
        mockMvc.perform(get("/user/jarfix01@gmail.com")
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testIfGetSingleUserRouteReturnsStatus403() throws Exception {
        Mockito.when(adminService.getUser("jarfix01@gmail.com")).thenReturn(authUser);
        mockMvc.perform(get("/user/jarfix01@gmail.com")
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "CRUD_USERS")
    void testIfGetProfilerRouteWorks() throws Exception {
        Mockito.when(adminService.getUser("jarfix01@gmail.com")).thenReturn(authUser);
        mockMvc.perform(get("/profile")
                        .header("Authorization", "Bearer Token")
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testIfGetProfilerRouteReturnsStatus401() throws Exception {
        Mockito.when(adminService.getUser("jarfix01@gmail.com")).thenReturn(authUser);
        mockMvc.perform(get("/profile")
                        .header("Authorization", "Bearer Token")
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}