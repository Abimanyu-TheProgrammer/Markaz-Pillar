package com.markaz.pillar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markaz.pillar.models.User;
import com.markaz.pillar.service.AuthService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private User user;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testRegisterIfInputIsAllCorrect() throws Exception {
        long num = Long.parseLong("85846001404");
        User user = new User();
        user.setEmail("jarfix01@gmail.com");
        user.setUsername("mofo01");
        user.setFullName("Abimanyu Yuda Dewa");
        user.setPhoneNum(num);
        user.setAddress("di Jalan");
        user.setPassword("1234567a");

        mockMvc.perform(post("/api/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isCreated());
    }

    @Test
    void testRegisterIfOneFieldIsMissing() throws Exception {
        long num = Long.parseLong("85846001404");
        User user = new User();
        user.setUsername("mofo01");
        user.setFullName("Abimanyu Yuda Dewa");
        user.setPhoneNum(num);
        user.setAddress("di Jalan");
        user.setPassword("1234567a");

        mockMvc.perform(post("/api/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterIfAllInputIsBlank() throws Exception {
        User user = new User();
        user.setUsername("");
        user.setFullName("");
        user.setAddress("");
        user.setPassword("");

        mockMvc.perform(post("/api/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterIfPasswordDoesntHave8Chars() throws Exception {
        long num = Long.parseLong("85846001404");
        User user = new User();
        user.setEmail("jarfix01@gmail.com");
        user.setUsername("mofo01");
        user.setFullName("Abimanyu Yuda Dewa");
        user.setPhoneNum(num);
        user.setAddress("di Jalan");
        user.setPassword("123");

        mockMvc.perform(post("/api/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterIfPasswordDoensntHaveNumbers() throws Exception {
        long num = Long.parseLong("85846001404");
        User user = new User();
        user.setEmail("jarfix01@gmail.com");
        user.setUsername("mofo01");
        user.setFullName("Abimanyu Yuda Dewa");
        user.setPhoneNum(num);
        user.setAddress("di Jalan");
        user.setPassword("abcdefghij");

        mockMvc.perform(post("/api/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterIfPasswordDoensntHaveLetters() throws Exception {
        long num = Long.parseLong("85846001404");
        User user = new User();
        user.setEmail("jarfix01@gmail.com");
        user.setUsername("mofo01");
        user.setFullName("Abimanyu Yuda Dewa");
        user.setPhoneNum(num);
        user.setAddress("di Jalan");
        user.setPassword("12345678");

        mockMvc.perform(post("/api/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest());
    }
}
