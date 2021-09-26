package com.markaz.pillar.service;

import com.markaz.pillar.models.User;
import com.markaz.pillar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public String register(User data){
        Optional<User> existingUser = userRepository.findByEmail(data.getEmail());

        if (existingUser.isPresent())
            throw new DataIntegrityViolationException("Email is already associated with a user");

        data.setPassword(passwordEncoder.encode(data.getPassword()));
        try {
            userRepository.saveAndFlush(data);
            return "New User Saved";
        } catch (Exception ex) {
            throw ex;
        }
    }
}


