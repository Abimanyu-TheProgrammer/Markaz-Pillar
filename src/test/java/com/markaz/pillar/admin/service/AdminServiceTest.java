package com.markaz.pillar.admin.service;

import com.markaz.pillar.auth.jwt.config.JwtTokenUtil;
import com.markaz.pillar.auth.jwt.service.JwtUserDetailsService;
import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.auth.repository.models.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    private static AuthUser authUser;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private JwtUserDetailsService userDetailsService;

    @BeforeAll
    public static void setUp() {
        Role role = new Role();
        role.setName("SUPERUSER");

        authUser = new AuthUser();
        authUser.setEmail("jarfix01@gmail.com");
        authUser.setRole(role);
        authUser.setUsername("mofo01");
        authUser.setFullName("Abimanyu Yuda Dewa");
        authUser.setPhoneNum("085846001404");
        authUser.setAddress("di Jalan");
        authUser.setPassword("admin123");
    }

    @Test
    void testIfGetAllUsersMethodWorks() {
        ArrayList<AuthUser> res = new ArrayList<>();
        res.add(authUser);
        Mockito.when(userRepository.findAll()).thenReturn(res);

        adminService.getAllUsers();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testIfGetUserMethodWorks() throws Exception {
        Mockito.when(userRepository.findByEmail("jarfix01@gmail.com")).thenReturn(Optional.of(authUser));

        adminService.getUser("jarfix01@gmail.com");
        verify(userRepository, times(1)).findByEmail("jarfix01@gmail.com");
    }

    @Test
    void testIfGetProfileMethodWorks() throws Exception {
        Mockito.when(userRepository.findByEmail("jarfix01@gmail.com")).thenReturn(Optional.of(authUser));

        UserDetails user = userDetailsService.loadUserByUsername("jarfix01@gmail.com");

        AuthUser credentials = new AuthUser();
        credentials.setEmail("jarfix01@gmail.com");
        credentials.setPassword("admin123");

        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(user, credentials);
        SecurityContextHolder.getContext().setAuthentication(authReq);

        adminService.getProfile();
        verify(userRepository, times(2)).findByEmail("jarfix01@gmail.com");
    }
}