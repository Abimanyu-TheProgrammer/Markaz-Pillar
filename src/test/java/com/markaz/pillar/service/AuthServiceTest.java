package com.markaz.pillar.service;

import com.markaz.pillar.models.User;
import com.markaz.pillar.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void testIfRegisterMethodWorks() {
        long num = Long.parseLong("85846001404");
        User user = new User();
        user.setEmail("jarfix01@gmail.com");
        user.setUsername("mofo01");
        user.setFullName("Abimanyu Yuda Dewa");
        user.setPhoneNum(num);
        user.setAddress("di Jalan");
        user.setPassword("12345678");

        Mockito.when(userRepository.save(user)).thenReturn(user);

        User savedUser = authService.register(user);
        assertThat(savedUser).isEqualTo(user);
    }
}
