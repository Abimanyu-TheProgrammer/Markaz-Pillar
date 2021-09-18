package com.markaz.pillar.service;

import com.markaz.pillar.models.User;
import com.markaz.pillar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User data){
        data.setPassword(passwordEncoder.encode(data.getPassword()));
        try {
            userRepository.save(data);
            return data;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }
}


