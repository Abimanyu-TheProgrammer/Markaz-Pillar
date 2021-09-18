package com.markaz.pillar.service;

import com.markaz.pillar.models.User;
import com.markaz.pillar.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.ibm.icu.impl.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AuthServiceTest {

    public static User user;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    public static void setUp() {
        long num = Long.parseLong("85846001404");
        user = new User();
        user.setEmail("jarfix01@gmail.com");
        user.setUsername("mofo01");
        user.setFullName("Abimanyu Yuda Dewa");
        user.setPhoneNum(num);
        user.setAddress("di Jalan");
        user.setPassword("12345678");
    }

        @Test
    void testIfRegisterMethodWorks() {


        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);
        Mockito.when(userRepository.findByEmail("jarfix01@gmail.com")).thenReturn(Optional.empty());

        authService.register(user);
        verify(userRepository, times(1)).findByEmail("jarfix01@gmail.com");
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    void testIfRegisterMethodThrowsDataIntegrityViolationIfUserExists() {

        Mockito.when(userRepository.findByEmail("jarfix01@gmail.com")).thenReturn(Optional.of(user));

        try{
            authService.register(user);
            verify(userRepository, times(1)).findByEmail("jarfix01@gmail.com");
            fail("Expected Data Integrity Exception");
        } catch(Exception e) {
            assertEquals(e.getClass(), DataIntegrityViolationException.class);
        }
    }
}
