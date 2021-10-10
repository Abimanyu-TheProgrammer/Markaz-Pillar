package com.markaz.pillar.auth.service;

import com.markaz.pillar.auth.repository.RoleRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.ibm.icu.impl.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {
    private static AuthUser authUser;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    public static void setUp() {
        authUser = new AuthUser();
        authUser.setEmail("jarfix01@gmail.com");
        authUser.setUsername("mofo01");
        authUser.setFullName("Abimanyu Yuda Dewa");
        authUser.setPhoneNum("085846001404");
        authUser.setAddress("di Jalan");
        authUser.setPassword("admin123");
    }

    @Test
    void testIfRegisterMethodWorks() {
        Role role = new Role();
        role.setId(1);
        role.setName("MEMBER");

        Mockito.when(roleRepository.findByName("MEMBER")).thenReturn(role);
        Mockito.when(userRepository.saveAndFlush(authUser)).thenReturn(authUser);
        Mockito.when(userRepository.findByEmail("jarfix01@gmail.com")).thenReturn(Optional.empty());

        registrationService.register(authUser);
        verify(userRepository, times(1)).findByEmail("jarfix01@gmail.com");
        verify(userRepository, times(1)).saveAndFlush(authUser);
    }

    @Test
    void testIfRegisterMethodThrowsDataIntegrityViolationIfUserExists() {
        Mockito.when(userRepository.findByEmail("jarfix01@gmail.com")).thenReturn(Optional.of(authUser));

        try{
            registrationService.register(authUser);
            verify(userRepository, times(1)).findByEmail("jarfix01@gmail.com");
            fail("Expected IllegalArgumentException");
        } catch(Exception e) {
            assertEquals(e.getClass(), IllegalArgumentException.class);
        }
    }
}
